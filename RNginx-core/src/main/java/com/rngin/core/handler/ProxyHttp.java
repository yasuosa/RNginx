package com.rngin.core.handler;

import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.net.SocketAddress;
import io.vertx.httpproxy.HttpProxy;
import io.vertx.httpproxy.ProxyInterceptor;
import io.vertx.httpproxy.ProxyOptions;
import io.vertx.httpproxy.impl.ReverseProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @program: RNginx
 * @description: 核心代码
 * @author: 任鹏宇
 * @create: 2022-06-30 15:47
 **/
public class ProxyHttp implements HttpProxy {

    public static ProxyHttp reverseProxy(HttpClient client) {
        return new ProxyHttp(client);
    }

    /**
     * 上游结点
     */
    private ConcurrentHashMap<SocketAddress, ReverseProxy> upstreams;

    private ConcurrentHashMap<String, SocketAddress> socketAddress;

    private List<String> upstreamSocketNames;


    private static final String PREFIX_SOCKET = "upstream_stock_";


    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private HttpClient client;


    public ProxyHttp(HttpClient client) {
        this.client = client;
        this.upstreams = new ConcurrentHashMap<>();
        this.socketAddress = new ConcurrentHashMap<>();
        this.upstreamSocketNames = Collections.synchronizedList(new ArrayList<>());
    }


    @Override
    public HttpProxy origin(int port, String host) {
        String socketName = getSocketName(host, port);
        if (!this.socketAddress.containsKey(socketName)) {
            // 添加连接
            SocketAddress socketAddress = SocketAddress.inetSocketAddress(port, host);

            ReverseProxy reverseProxy = new ReverseProxy(new ProxyOptions(), client);


            reverseProxy.originSelector((req -> Future.succeededFuture(socketAddress)));

            this.socketAddress.put(socketName, socketAddress);
            this.upstreamSocketNames.add(socketName);
            this.upstreams.put(socketAddress, reverseProxy);
        }
        return this;
    }



    @Override
    public HttpProxy originSelector(Function<HttpServerRequest, Future<SocketAddress>> selector) {
        //upstreams.forEach((key, value) -> value.originSelector(req -> Future.succeededFuture(key)));
        return null;
    }


    @Override
    public HttpProxy addInterceptor(ProxyInterceptor interceptor) {
        for (ReverseProxy proxy : upstreams.values()) {
            proxy.addInterceptor(interceptor);
        }
        return this;
    }

    @Override
    public void handle(HttpServerRequest outboundRequest) {
        SocketAddress socketAddress = this.socketAddress.get(getSocketName());
        Objects.requireNonNull(socketAddress,"配置连接信息异常！");
        upstreams.get(socketAddress).handle(outboundRequest);
    }


    private String getSocketName(String host, int port) {
        return new StringBuffer(PREFIX_SOCKET)
                .append(host).append("_")
                .append(port).append("_")
                .append(this.upstreamSocketNames.size()).toString();
    }

    private String getSocketName() {
        // 负载均衡算法
        int index = atomicInteger.getAndIncrement();
        return this.upstreamSocketNames.get(index % this.upstreamSocketNames.size());
    }


}

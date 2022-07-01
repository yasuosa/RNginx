package com.rngin.core.handler;

import com.rngin.core.lb.LoadBalanceContext;
import com.rnginx.common.entriy.LoadBalance;
import com.rnginx.common.exception.ProxyConfigException;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.net.SocketAddress;
import io.vertx.httpproxy.HttpProxy;
import io.vertx.httpproxy.ProxyInterceptor;
import io.vertx.httpproxy.ProxyOptions;
import io.vertx.httpproxy.impl.ReverseProxy;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
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
    private Map<SocketAddress, ReverseProxy> upstreams;

    private Map<String, SocketAddress> socketAddress;

    private List<String> upstreamSocketNames;


    private static final String PREFIX_SOCKET = "upstream_stock_";

    private HttpClient client;

    private LoadBalance lb; // 负载均衡 类型

    private TreeMap<Double,String> weights;

    private Double curWeight = (double) 0;


    private LoadBalanceContext loadBalanceContext;





    public ProxyHttp(HttpClient client) {
        this.client = client;
        this.upstreams = new HashMap<>();
        this.socketAddress = new HashMap<>();
        this.upstreamSocketNames = new ArrayList<>();
        this.weights = new TreeMap<>();
        this.lb = LoadBalance.RANDOM;
        this.loadBalanceContext = new LoadBalanceContext();
    }


    /**
     * 设置负载均衡算法
     * @param lb
     * @return
     */
    public HttpProxy type(LoadBalance lb){
        this.lb = lb;
        return this;
    }


    public HttpProxy origin(String address) {
        if(LoadBalance.WEIGHT.equals(lb)) {
           List<String> weightData=  doGetWeightData(address);
            URI uri = getUri(weightData.get(0));
            return origin(uri.getPort(), uri.getHost(), curWeight = (curWeight+ Double.parseDouble(weightData.get(1))));
        }

        URI uri = getUri(address);
        return origin(uri.getPort(), uri.getHost());
    }


    public HttpProxy origin(int port, String host,Double weight) {
        if(!LoadBalance.WEIGHT.equals(lb)){
            weight = null;
        }

        String socketName = getSocketName(host, port);
        if (!this.socketAddress.containsKey(socketName)) {
            // 添加连接
            SocketAddress socketAddress = SocketAddress.inetSocketAddress(port, host);

            ReverseProxy reverseProxy = new ReverseProxy(new ProxyOptions(), client);


            reverseProxy.originSelector((req -> Future.succeededFuture(socketAddress)));

            this.socketAddress.put(socketName, socketAddress);
            this.upstreamSocketNames.add(socketName);
            this.upstreams.put(socketAddress, reverseProxy);

            if(null != weight){
                this.weights.put(weight,socketName);
            }
        }
        return this;
    }


    @Override
    public HttpProxy origin(SocketAddress address) {
        int port = address.port();
        String host = address.host();
        address = null; // 回收
        return origin(port, host);
    }



    @Override
    public HttpProxy origin(int port, String host) {
        origin(port,host,null);
        return this;
    }


    @Override
    public HttpProxy originSelector(Function<HttpServerRequest, Future<SocketAddress>> selector) {
        throw new ProxyConfigException("没有意义");
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
        if(!loadBalanceContext.isCompact()){
            loadBalanceContext.init(upstreamSocketNames,curWeight,weights,lb);
        }
        SocketAddress socketAddress = this.socketAddress.get(loadBalanceContext.getSocketName(outboundRequest));
        Objects.requireNonNull(socketAddress,"配置连接信息异常！");
        upstreams.get(socketAddress).handle(outboundRequest);
    }


    private String getSocketName(String host, int port) {
        return new StringBuffer(PREFIX_SOCKET)
                .append(host).append("_")
                .append(port).append("_")
                .append(this.upstreamSocketNames.size()).toString();
    }




    /**
     * 获取权重
     * @param address
     * @return
     */
    private List<String> doGetWeightData(String address) {
        String[] addr = address.split(" ");
        List<String> data = new ArrayList<>();
        for (String s : addr) {
            if("".equals(s.trim())){
                continue;
            }
            data.add(s.trim());
        }
        if(data.size() != 2){
            throw new Error("负载均衡-权重配置文件错误! " + address);
        }
        return data;
    }

    private URI getUri(String address){
        URI uri = null;
        try {
            uri = new URI(address);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }
}

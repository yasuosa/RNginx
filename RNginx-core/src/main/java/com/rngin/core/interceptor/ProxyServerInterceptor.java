package com.rngin.core.interceptor;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.httpproxy.*;
import io.vertx.httpproxy.impl.BufferedReadStream;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @program: RNginx
 * @description: 代理配置拦截器
 * @author: 任鹏宇
 * @create: 2022-06-30 14:01
 **/
@Slf4j
public class ProxyServerInterceptor implements ProxyInterceptor {


    /**
     * 处理代理请求
     *
     * @param context
     * @return
     */
    @Override
    public Future<ProxyResponse> handleProxyRequest(ProxyContext context) {
        long startTime = System.currentTimeMillis();
        ProxyRequest request = context.request();
        request.proxiedRequest().bodyHandler(buf -> {
            String income = new String(buf.getBytes());
            context.set("startTime", startTime);
            context.set("data", income);
        });
        return context.sendRequest();
    }


    /**
     * 处理代理返回
     *
     * @param context
     * @return
     */
    @Override
    public Future<Void> handleProxyResponse(ProxyContext context) {
        ProxyResponse response = context.response();
        response.headers().add("HeiHeiHei", "RNginx/Rpy");

        Long startTime = context.get("startTime", Long.class);
        String data = context.get("data", String.class);

        ProxyRequest request = response.request();
        log.info("状态:{} - {} - {} \n 入参:{} - 耗时:{}", response.getStatusCode(),
                request.getMethod(), request.getURI(),
                data, System.currentTimeMillis() - startTime);
        return context.sendResponse();
    }
}

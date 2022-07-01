package com.rngin.core.handler;

import io.netty.handler.proxy.ProxyHandler;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.proxy.handler.impl.ProxyHandlerImpl;
import io.vertx.httpproxy.HttpProxy;

/**
 * @program: RNginx
 * @description:  稍等加强
 * @author: 任鹏宇
 * @create: 2022-06-30 15:26
 **/
public class ProxyServerHandler extends ProxyHandlerImpl {

    private ProxyHttp proxy;

    public ProxyServerHandler(HttpProxy httpProxy) {
        super(httpProxy);
        this.proxy = (ProxyHttp) httpProxy;
    }


    @Override
    public void handle(RoutingContext ctx) {


        //System.out.println("请求前");
        super.handle(ctx);

        //System.out.println("请求后");
    }
}

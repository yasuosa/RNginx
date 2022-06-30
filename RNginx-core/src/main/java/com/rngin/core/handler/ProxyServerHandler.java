package com.rngin.core.handler;

import io.netty.handler.proxy.ProxyHandler;
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

    public ProxyServerHandler(HttpProxy httpProxy) {
        super(httpProxy);
    }


    @Override
    public void handle(RoutingContext ctx) {

        super.handle(ctx);
    }
}

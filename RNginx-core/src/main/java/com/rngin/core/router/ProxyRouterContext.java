package com.rngin.core.router;

import com.rnginx.common.entriy.config.BaseProxyConfig;
import com.rnginx.common.entriy.config.ProxyConfig;
import com.rnginx.common.entriy.config.ProxyServerConfig;
import com.rnginx.common.exception.ProxyConfigException;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.impl.RouterImpl;
import io.vertx.ext.web.proxy.handler.ProxyHandler;
import io.vertx.httpproxy.HttpProxy;

import java.awt.*;
import java.util.List;

/**
 * @program: RNginx
 * @description: 代理路由配置
 * @author: 任鹏宇
 * @create: 2022-06-30 08:59
 **/
public class ProxyRouterContext implements IRouterHandle<Router, Void> {

    private Vertx vertx;

    private ProxyServerConfig config;

    public static Router create(Vertx vertx) {
        return new ProxyRouterContext(vertx).create();
    }


    public ProxyRouterContext(Vertx vertx) {
        this.vertx = vertx;
        obtainConfig();
    }

    /**
     * 创建router
     *
     * @return
     */
    private Router doCreate() {
        Router router = Router.router(vertx);
        // 开始页面
        router.get("/").handler(StaticHandler.create("webroot\\root")
                        .setAllowRootFileSystemAccess(true)
                        .setCachingEnabled(false)); // 是否缓存

        List<ProxyConfig> proxyConfigList = config.getProxyConfigList();
        if (null != proxyConfigList) {
            proxyConfigList.forEach(proxyConfig -> {
                doCreatRoute(router, proxyConfig);
            });
        }
        // 异常处理
        handlerExceptionHandler(router);
        return router;
    }



    /**
     * 创建路由
     *
     * @param router
     * @param proxyConfig
     */
    private void doCreatRoute(Router router, ProxyConfig proxyConfig) {
        Route route = router.route(proxyConfig.getLocation());

        // 静态资源转发
        if (proxyConfig.isStaticResource()) {
            route.handler(rc -> {
                if (!proxyConfig.getCache()) {
                    rc.response().headers()
                            .add("Cache-Control", "no-store")
                            .add("Cache-Control", "no-cache");
                }
                rc.next();
            }).handler(StaticHandler.create()
                    .setAllowRootFileSystemAccess(true)
                    .setWebRoot(proxyConfig.getRoot())
                    .setCachingEnabled(proxyConfig.getCache()) // 是否缓存
                    .setMaxAgeSeconds(proxyConfig.getMaxAgeSeconds())); // 是否
        }

        // 路由转发
        else {
            route.handler(LoggerHandler.create())
                    .handler(ProxyHandler.create(HttpProxy.reverseProxy(vertx.createHttpClient())
                            .origin(proxyConfig.getPort(), proxyConfig.getHost())));
        }

    }

    /**
     * 404
     * 500
     *
     * @param router
     */
    private void handlerExceptionHandler(Router router) {
        // 异常处理
        router.errorHandler(404, rc -> {
            rc.response().end("404");
        });

        router.errorHandler(500, rc -> {
            rc.response().end("500");
        });

    }


    @Override
    public Router create() {
        return doCreate();
    }

    @Override
    public Router create(Void config) {
        return create();
    }


    /**
     * 获取配置信息
     */
    private void obtainConfig() {
        LocalMap<Object, Object> configMap = vertx.sharedData().getLocalMap(BaseProxyConfig.CONFIG);
        config = (ProxyServerConfig) configMap.get(BaseProxyConfig.CONFIG);
    }
}

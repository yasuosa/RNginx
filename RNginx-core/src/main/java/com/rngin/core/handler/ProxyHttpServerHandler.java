package com.rngin.core.handler;

import com.rnginx.common.constant.ProxyConstant;
import com.rnginx.common.entriy.config.BaseProxyConfig;
import com.rnginx.common.entriy.config.ProxyConfig;
import com.rnginx.common.entriy.config.ProxyServerConfig;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.*;
import io.vertx.core.shareddata.LocalMap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: RNginx
 * @description: 代理处理器基类
 * @author: 任鹏宇
 * @create: 2022-06-29 15:24
 **/
@Slf4j
@Deprecated
public class ProxyHttpServerHandler implements Handler<HttpServerRequest> {


    protected Vertx vertx;

    private ProxyServerConfig config;

    public ProxyHttpServerHandler(Vertx vertx) {
        this.vertx = vertx;
        obtainConfig();
    }

    private void obtainConfig() {
        LocalMap<Object, Object> configMap = vertx.sharedData().getLocalMap(BaseProxyConfig.CONFIG);
        config = (ProxyServerConfig) configMap.get(BaseProxyConfig.CONFIG);
    }


    @Override
    public void handle(HttpServerRequest request) {
        List<ProxyConfig> proxyConfigList = config.getProxyConfigList();
        String path = request.path();
        String uri = request.uri();

        // 返回题
        HttpServerResponse response = request.response();


        request.pause(); // 暂停

        // 404

        for (ProxyConfig proxyConfig : proxyConfigList) {
            String location = proxyConfig.getLocation();
            // 命中
            if (uri.startsWith(location)) {
                uri = uri.replaceFirst(location, proxyConfig.getUri());
                HttpClient client = vertx.createHttpClient();
                client.request(request.method(), proxyConfig.getPort(), proxyConfig.getHost(), uri, ar -> {
                    // 代理request构造成功
                    if (ar.succeeded()) {
                        HttpClientRequest upStreamReq = ar.result();
                        upStreamReq.headers().setAll(request.headers()); // 传入headers
                        upStreamReq.send(request)
                                .onSuccess(upStreamResq -> {
                                    response.headers().setAll(upStreamResq.headers());
                                    response.headers().add("Koi", "RNginx/RPY");
                                    response.send(upStreamResq);
                                })
                                .onFailure(t -> {
                                    backErrorMessage(response, t);
                                });
                    }

                    // 代理失败
                    else {
                        ar.cause().printStackTrace();
                        backErrorMessage(response, ar.cause());
                    }
                });

                break;
            }

        }


    }

    /**
     * 异常处理
     *
     * @param response
     * @param t
     */
    private void backErrorMessage(HttpServerResponse response, Throwable t) {
        t.printStackTrace();
        response.setStatusCode(ProxyConstant.ERROR);
        response.end("请求失败！" + t.getMessage());
    }


}

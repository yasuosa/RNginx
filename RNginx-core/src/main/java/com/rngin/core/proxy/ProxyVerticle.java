package com.rngin.core.proxy;

import com.rngin.core.handler.ProxyHttpServerHandler;
import com.rngin.core.router.ProxyRouterContext;
import com.rnginx.common.entriy.config.BaseProxyConfig;
import com.rnginx.common.entriy.config.ProxyServerConfig;
import com.rnginx.common.log.LogTip;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @program: RNginx
 * @description: 代理总类
 * @author: 任鹏宇
 * @create: 2022-06-29 15:18
 **/
@Slf4j
public class ProxyVerticle extends AbstractVerticle {




    /**
     * 根据配置文件 请求转发
     * 1.常规请求 GET/POST/DELETE/... 转发
     * 2.文件上传 流处理 转发
     * 3.websocket 转发
     * @throws Exception
     */
    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        // 获取conf/config.json 的配置信息
        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.getConfig( ar -> {
            if(ar.succeeded()){
                JsonObject config = ar.result();
                log.info(LogTip.CONFIG_SUCCEED,config.toString());
                startProxyServer(vertx,ar.result());
            }
            else{
                ar.cause().printStackTrace();
                log.warn(LogTip.CONFIG_SUCCEED,ar.cause().getMessage());
            }
        });
    }


    /**
     * 开启代理服务
     * @param vertx
     * @param result
     */
    private void startProxyServer(Vertx vertx, JsonObject result) {
        // 将配置共享
        Integer port = doShareConfigData(vertx, result);



        vertx.createHttpServer()
                //.requestHandler(new ProxyHttpServerHandler(vertx))
                .requestHandler(ProxyRouterContext.create(vertx))
                .listen(port)
                .onSuccess( event ->  {
                    log.info(LogTip.SERVER_SUCCEED,port);
                })
                .onFailure( event ->  {
                    event.printStackTrace();
                    log.warn(LogTip.SERVER_FAILED, event.getMessage());
                });
    }


    /**
     * 设置共享数据
     * @param vertx
     * @param config
     */
    private Integer doShareConfigData(Vertx vertx, JsonObject config) {
        LocalMap<String, Object> localMap = vertx.sharedData().getLocalMap(BaseProxyConfig.CONFIG);
        ProxyServerConfig proxyServerConfig = new ProxyServerConfig(config);
        localMap.put(BaseProxyConfig.CONFIG,proxyServerConfig);
        return proxyServerConfig.getPort();
    }


}

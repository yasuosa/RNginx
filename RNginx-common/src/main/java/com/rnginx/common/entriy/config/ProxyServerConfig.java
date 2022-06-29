package com.rnginx.common.entriy.config;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: RNginx
 * @description: 代理服务器配置
 * @author: 任鹏宇
 * @create: 2022-06-29 16:04
 **/
@Getter
@Setter
public class ProxyServerConfig extends BaseProxyConfig  implements IProxyConfig<ProxyServerConfig>{


    // 默认端口
    private static final Integer DEFAULT_PORT = 9999;

    // 默认名称
    private static final String DEFAULT_SERVER_NAME = "RNGINX_PROXY_SERVER";



    private Integer port;

    private String name;


    private List<ProxyConfig> proxyConfigList;


    public ProxyServerConfig(){
        this.port = DEFAULT_PORT;
        this.name =DEFAULT_SERVER_NAME;
        this.proxyConfigList = new ArrayList<>();
    }


    public ProxyServerConfig(JsonObject config){
        this();
        initConfig(config);
    }



    @Override
    public void initConfig(JsonObject config) {
        JsonObject proxyServerConfig = config.getJsonObject(PROXY_SERVER);
        if(null != proxyServerConfig){
            this.port = proxyServerConfig.getInteger(PROXY_SERVER_KEY_PORT,DEFAULT_PORT);
            this.name = proxyServerConfig.getString(PROXY_SERVER_KEY_NAME,DEFAULT_SERVER_NAME);
            initProxyConfig(config);
        }
    }



    /**
     * 配置代理类对象
     * @param config
     */
    private void initProxyConfig(JsonObject config) {
        this.proxyConfigList = new ArrayList<>();
        JsonArray proxyConfigList = config.getJsonArray(PROXY);
        if(null != proxyConfigList){
            proxyConfigList.forEach(jsonObj -> {
                ProxyConfig proxyConfig = new ProxyConfig();
                proxyConfig.getConfig((JsonObject) jsonObj);
                this.proxyConfigList.add(proxyConfig);
            });
        }
    }

    @Override
    public ProxyServerConfig getConfig() {
        return this;
    }

    @Override
    public ProxyServerConfig getConfig(JsonObject config) {
        initConfig(config);
        return this;
    }

}

package com.rnginx.common.entriy.config;

import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @program: RNginx
 * @description: 代理类
 * @author: 任鹏宇
 * @create: 2022-06-29 16:11
 **/
@Getter
@Setter
public class ProxyConfig extends BaseProxyConfig implements IProxyConfig<ProxyConfig> {

    // 默认路径
    private static final String DEFAULT_PATH = "/";

    // 前端路径默认
    private static final String DEFAULT_ROOT_ABS_PATH = DEFAULT_PATH;


    // 匹配路径 支持正则
    private String location;


    // 代理路径
    private String proxyPass;


    // 前端路径
    private String root;


    private String host;

    private Integer port;

    private String uri;




    public ProxyConfig() {

    }


    public ProxyConfig(JsonObject jsonObject){
        initConfig(jsonObject);
    }




    @Override
    public void initConfig(JsonObject config) {
        URI uri = null;
        try {
            this.location = config.getString(PROXY_KEY_LOCATION);
            this.proxyPass = config.getString(PROXY_KEY_PROXY_PASS);
            this.root = config.getString(PROXY_KEY_ROOT);
            // TODO dns需要解析
            if(null != this.proxyPass){
                uri = new URI(this.proxyPass );
                this.host = uri.getHost();
                this.uri = uri.getPath();
                this.port = uri.getPort();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ProxyConfig getConfig() {
        return this;
    }

    @Override
    public ProxyConfig getConfig(JsonObject config) {
        initConfig(config);
        return this;
    }


}

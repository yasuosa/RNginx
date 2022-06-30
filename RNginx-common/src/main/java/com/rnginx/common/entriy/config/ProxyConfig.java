package com.rnginx.common.entriy.config;

import com.sun.jndi.toolkit.url.Uri;
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


    /*-------------------- 路由配置参数开始 ----------------------*/
    // 匹配路径 支持正则
    private String location;

    // 上游名称
    private String upstream;

    // 代理路径
    private String proxyPass;

    /*-------------------- 路由配置参数开始 ----------------------*/



   /*-------------------- 静态资源配置参数开始 ----------------------*/
    private String root; // 静态资源根目录

    private Boolean cache;  // 是否缓存

    private Long maxAgeSeconds; // 缓存时间

    /*-------------------- 静态资源配置参数结束 ----------------------*/



    private String host;

    private Integer port;

    private String uri;

    // 静态资源
    private boolean staticResource;

    private boolean upstreamResource;





    public ProxyConfig() {

    }


    public ProxyConfig(JsonObject jsonObject){
        initConfig(jsonObject);
    }




    @Override
    public void initConfig(JsonObject config) {
        try {
            this.location = config.getString(PROXY_KEY_LOCATION);
            this.proxyPass = config.getString(PROXY_KEY_PROXY_PASS);
            this.root = config.getString(PROXY_KEY_ROOT);
            this.cache = config.getBoolean(PROXY_KEY_CACHE,false);
            this.maxAgeSeconds = config.getLong(PROXY_KEY_MAX_AGE_SECONDS, (long) (24 * 6000 * 60));
            this.upstream = config.getString(UPSTREAM);

            // 优先级 静态 > 上流 > 普通代理
            // TODO dns需要解析  域名类 目前没有写 可能需要dns解析 得到ip:port

            if(null != this.root){
                doHandlerStaticResource();
                return;
            }

            if(null != this.upstream){
                doHandlerUpstream();
                return;
            }

            if(null != this.proxyPass){
                doHandlerProxyPass();
                //return;
            }


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理普通代理
     */
    private void doHandlerProxyPass() throws URISyntaxException {
        URI uri = new URI(this.proxyPass );
        this.host = uri.getHost();
        this.uri = uri.getPath();
        this.port = uri.getPort();
    }


    /**
     * 处理上流结点配置
     */
    private void doHandlerUpstream() {
        this.upstreamResource = true;
    }

    /**
     * 处理静态资源
     */
    private void doHandlerStaticResource() {
        this.staticResource = true;
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

package com.rnginx.common.entriy.config;

import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: RNginx
 * @description: 配置类
 * @author: 任鹏宇
 * @create: 2022-06-29 16:12
 **/
@Getter
@Setter
public  class BaseProxyConfig implements Serializable {


    protected Vertx vertx;

    public static final String CONFIG= "config";




    public static final String PROXY_SERVER= "server";
    public static final String PROXY= "proxy";
    public static final String UPSTREAM= "upstream";






    /**
     * ProxyServerConfig
     */
    public static final String PROXY_SERVER_KEY_PORT = "port";
    public static final String PROXY_SERVER_KEY_NAME = "name";


    /**
     * ProxyConfig
     */
    public static final String PROXY_KEY_LOCATION= "location";
    public static final String PROXY_KEY_PROXY_PASS = "proxy_pass";
    public static final String PROXY_KEY_ROOT= "root";
    public static final String PROXY_KEY_CACHE= "cache";
    public static final String PROXY_KEY_MAX_AGE_SECONDS= "maxAgeSeconds";



    /**
     * upstream
     */
    public static final String UPSTREAM_KEY_NAME = "name";
    public static final String UPSTREAM_KEY_TYPE = "type";
    public static final String UPSTREAM_KEY_NODES= "nodes";

}

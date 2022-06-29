package com.rnginx.common.entriy.config;

import io.vertx.core.json.JsonObject;

/**
 * @program: RNginx
 * @description: 配置接口
 * @author: 任鹏宇
 * @create: 2022-06-29 16:43
 **/
public interface IProxyConfig<T> {


    /**
     * 配置初始化
     * @param config
     */
    void initConfig(JsonObject config);


    /**
     * 获取配置
     * @return T
     */
    T getConfig();

    /**
     * 获取配置
     * @param jsonObject 传入配置参数, 如果没有则默认初始化
     * @return T
     */
    T getConfig(JsonObject config);

}

package com.rngin.core.lb;

import io.vertx.core.http.HttpServerRequest;

/**
 * @program: RNginx
 * @description: 负载均衡
 * @author: 任鹏宇
 * @create: 2022-06-30 18:37
 **/
public interface ILoadBalance<T> {


    /**
     * 选择
     * @return
     */
    T select(HttpServerRequest request);
}

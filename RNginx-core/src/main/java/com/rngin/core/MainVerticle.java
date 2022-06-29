package com.rngin.core;

import com.rngin.core.proxy.ProxyVerticle;
import io.vertx.core.Vertx;

/**
 * @program: RNginx
 * @description: 主入口测试
 * @author: 任鹏宇
 * @create: 2022-06-29 15:02
 **/
public class MainVerticle {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new ProxyVerticle());

    }
}

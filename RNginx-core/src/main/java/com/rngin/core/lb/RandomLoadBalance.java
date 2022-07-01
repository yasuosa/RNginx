package com.rngin.core.lb;

import io.vertx.core.http.HttpServerRequest;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @program: RNginx
 * @description: 随机算法
 * @author: 任鹏宇
 * @create: 2022-07-01 09:07
 **/
public class RandomLoadBalance extends AbstractLoadBalance{


    public RandomLoadBalance(List<String> selectors) {
        super(selectors);
    }

    @Override
    public String doSelect() {
        return selectors.get((int) (Math.random() * selectors.size()));
    }



    @Override
    public void signIp(String ip) {

    }
}

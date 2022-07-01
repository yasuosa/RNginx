package com.rnginx.common.entriy;

import lombok.Getter;

/**
 * @program: RNginx
 * @description: 负载均衡
 * @author: 任鹏宇
 * @create: 2022-07-01 09:32
 **/

@Getter
public enum LoadBalance {
    // 负载均衡算法
    ROBIN("robin","轮询"),
    WEIGHT("weight","权重"),
    IP_HASH("ip_hash","IP哈希"),
    FAIL("fail","最小反应时间"),
    RANDOM("random","随机"),

    ;

    // 名称
    private String name;

    // 描述
    private String desc;

    LoadBalance(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }



    public static LoadBalance nameOf(String name){
        for (LoadBalance value : values()) {
            if(value.name.equals(name))
                return value;
        }
        return null;
    }
}

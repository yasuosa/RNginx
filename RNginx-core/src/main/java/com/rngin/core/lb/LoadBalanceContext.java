package com.rngin.core.lb;

import com.rnginx.common.entriy.LoadBalance;
import io.vertx.core.http.HttpServerRequest;

import java.util.*;

/**
 * @program: RNginx
 * @description:
 * @author: 任鹏宇
 * @create: 2022-07-01 10:15
 **/
public class LoadBalanceContext {

    private boolean compact;

    private List<String> socketNames;

    private TreeMap<Double,String> weightMap;

    private Double weight;

    private LoadBalance lb;

    private Map<LoadBalance,AbstractLoadBalance> lMap;


    public void init(List<String> socketNames,Double weight,TreeMap<Double,String> weightMap,LoadBalance lb){
        this.socketNames = socketNames;
        this.weight = weight;
        this.weightMap = weightMap;
        this.compact = true;
        this.lMap = new HashMap<>();
        this.lb = lb;
        initLbMap();
    }

    /**
     * 初始化负载均衡器
     */
    private void initLbMap() {
        this.lMap.put(LoadBalance.ROBIN,new RobinLoadBalance(socketNames));
        this.lMap.put(LoadBalance.RANDOM,new RandomLoadBalance(socketNames));
        this.lMap.put(LoadBalance.WEIGHT,new WeightLoadBalance(socketNames,weightMap,weight));
        this.lMap.put(LoadBalance.FAIL,new FailLoadBalance(socketNames));
        this.lMap.put(LoadBalance.IP_HASH,new IpHashLoadBalance(socketNames));
    }


    public String getSocketName(HttpServerRequest request){
        return lMap.get(lb).select(request);
    }


    /**
     * 根据负载均衡算法 获取 socketName
     * @param request
     * @return
     */
    public String getSocketName(LoadBalance lb,HttpServerRequest request){
        if(!compact){
            throw new Error("尚未配置！");
        }
        this.lb = lb;
        return getSocketName(request);
    }

    public boolean isCompact() {
        return compact;
    }
}

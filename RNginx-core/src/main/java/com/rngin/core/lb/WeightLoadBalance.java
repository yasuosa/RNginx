package com.rngin.core.lb;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: RNginx
 * @description: 权重
 * @author: 任鹏宇
 * @create: 2022-07-01 09:05
 **/
@Slf4j
public class WeightLoadBalance extends AbstractLoadBalance {

    /***
     * 平面展开
     * w1 0.3
     * w2 0.4
     * w3 0.9
     * |_________________________________________|  = 0.3 + 0.4 + 0.9 =1.6
     * |________|___________|____________________|
     *     0.3       0.4             0.9
     *  w1 =0.3    w2 = 0.7         w3 =1.6
     *
     *  Math.random().nextDouble() * 1.6 = index
     */
    private TreeMap<Double,String> weights;

    private Double weight;

    public WeightLoadBalance(List<String> selectors, TreeMap<Double, String> weights, Double weight) {
        super(selectors);
        this.weights = weights;
        this.weight = weight;
    }

    @Override
    public void signIp(String ip) {
        // nothing
    }

    @Override
    public String doSelect() {
        Double selectWeight = weights.ceilingKey(Math.random() * weight);
        log.info("选择权重---"+selectWeight);
        return weights.get(selectWeight);
    }
}

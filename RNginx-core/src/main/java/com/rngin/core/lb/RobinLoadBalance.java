package com.rngin.core.lb;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: RNginx
 * @description: 轮询负载均衡
 * @author: 任鹏宇
 * @create: 2022-07-01 09:03
 **/

public class RobinLoadBalance extends AbstractLoadBalance{


    private AtomicInteger atomicInteger = new AtomicInteger(0);


    public RobinLoadBalance(List<String> selectors) {
        super(selectors);
    }

    @Override
    public void signIp(String ip) {
        // nothing
    }


    @Override
    public String doSelect() {
        int index = atomicInteger.getAndIncrement();
        return selectors.get(index % this.selectors.size());
    }
}

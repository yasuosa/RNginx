package com.rngin.core.lb;

import java.util.List;

/**
 * @program: RNginx
 * @description: 最短响应时间  暂未时间
 * @author: 任鹏宇
 * @create: 2022-07-01 09:07
 **/
public class FailLoadBalance extends AbstractLoadBalance{
    public FailLoadBalance(List<String> selectors) {
        super(selectors);
    }

    @Override
    public void signIp(String ip) {
        // nothing
    }

    @Override
    public String doSelect() {
        return null;
    }
}

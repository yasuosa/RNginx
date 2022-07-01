package com.rngin.core.lb;

import java.util.List;
import java.util.Map;

/**
 * @program: RNginx
 * @description: ip_hash
 * @author: 任鹏宇
 * @create: 2022-07-01 09:06
 **/
public class IpHashLoadBalance extends AbstractLoadBalance {

    private volatile String ip;


    public IpHashLoadBalance(List<String> selectors) {
        super(selectors);
    }

    @Override
    public void signIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String doSelect() {
        return selectors.get( (selectors.size() - 1) & hash(ip));
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}

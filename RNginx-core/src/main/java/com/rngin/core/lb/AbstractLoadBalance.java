package com.rngin.core.lb;

import com.rngin.core.utils.HttpUtils;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: RNginx
 * @description: 抽象基类
 * @author: 任鹏宇
 * @create: 2022-06-30 18:39
 **/
@Slf4j
public abstract class AbstractLoadBalance implements ILoadBalance<String> {


    /**
     * 带抽取的名称
     */
    protected List<String> selectors;


    public AbstractLoadBalance(List<String> selectors) {
        this.selectors = selectors;
    }

    @Override
    public String select(HttpServerRequest request) {
        String ip = HttpUtils.getIP(request);
        log.info("ip:{} - 请求地址:{}",ip,request.host() + request.path());

        signIp(ip); // 记录IP

        String socketName = doSelect();


        log.info("转发到-----" + socketName);


        return socketName;
    }



    public abstract void signIp(String ip);


    public abstract String doSelect();
}

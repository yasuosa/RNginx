package com.rngin.core.router;

/**
 * @program: RNginx
 * @description:
 * @author: 任鹏宇
 * @create: 2022-06-30 09:04
 **/
public interface  IRouterHandle<T,C> {


    /**
     * 创建RouterHandle
     * @return
     */
    T create();


    /**
     * 根据配置创建RouterHandle
     * @param config
     * @return
     */
    T create(C config);
}

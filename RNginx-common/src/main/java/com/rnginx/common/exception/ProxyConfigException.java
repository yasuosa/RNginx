package com.rnginx.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: RNginx
 * @description:
 * @author: 任鹏宇
 * @create: 2022-06-30 09:06
 **/
@Getter
@Setter
public class ProxyConfigException extends RuntimeException {

    private String message;


    public ProxyConfigException(String message) {
        this.message = message;
    }
}

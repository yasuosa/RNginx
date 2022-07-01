package com.rngin.core.utils;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: RNginx
 * @description:
 * @author: 任鹏宇
 * @create: 2022-07-01 11:15
 **/
@Slf4j
public class HttpUtils {


    // 多次反向代理后会有多个ip值 的分割符
    private final static String IP_UTILS_FLAG = ",";
    // 未知IP
    private final static String UNKNOWN = "unknown";
    // 本地 IP
    private final static String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private final static String LOCALHOST_IP1 = "127.0.0.1";

    public static String getIP(HttpServerRequest request) {
        // 根据 HttpHeaders 获取 请求 IP地址
        String ip = request.headers().get("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.headers().get("x-forwarded-for");
            if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (ip.contains(IP_UTILS_FLAG)) {
                    ip = ip.split(IP_UTILS_FLAG)[0];
                }
            }
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.headers().get("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.headers().get("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.headers().get("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.headers().get("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.headers().get("X-Real-IP");
        }
        //兼容k8s集群获取ip
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.localAddress().hostAddress();
            if (LOCALHOST_IP1.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                //根据网卡取本机配置的IP
                InetAddress iNet = null;
                try {
                    iNet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getClientIp error: ", e);
                }
                assert iNet != null;
                ip = iNet.getHostAddress();
            }
        }
        return ip;
    }
    

}

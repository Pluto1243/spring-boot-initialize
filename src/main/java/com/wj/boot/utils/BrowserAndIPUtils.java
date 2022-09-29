package com.wj.boot.utils;

import javax.servlet.http.HttpServletRequest;


/**
 * BrowserAndIP工具类
 *
 * @author wangjie
 * @date 11:21 2022年07月26日
 **/
public class BrowserAndIPUtils {

    private static final String UNKNOW = "unknow";

    public static String getIP(HttpServletRequest request) {
        String loginIp = request.getHeader("X-Forwarded-For");
        if (loginIp == null || loginIp.isEmpty() || UNKNOW == loginIp) {
            loginIp = request.getHeader("PRoxy-Client-IP");
        }
        if (loginIp == null || loginIp.isEmpty() || UNKNOW == loginIp) {
            loginIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (loginIp == null || loginIp.isEmpty() || UNKNOW == loginIp) {
            loginIp = request.getHeader("X-Real-IP");
        }
        if (loginIp == null || loginIp.isEmpty() || UNKNOW == loginIp) {
            loginIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (loginIp == null || loginIp.isEmpty() || UNKNOW == loginIp) {
            loginIp = request.getRemoteAddr();
        }
        return loginIp;
    }
}

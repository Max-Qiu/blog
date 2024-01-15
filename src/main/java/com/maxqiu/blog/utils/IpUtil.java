package com.maxqiu.blog.utils;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Max_Qiu
 */
@Component
public class IpUtil {
    /**
     * 获取用户真实IP地址
     *
     * @param servletRequest
     *            servletRequest
     * @return 返回IP
     */
    public String getIpAddress(HttpServletRequest servletRequest) {
        String ip = servletRequest.getHeader("x-forwarded-for");
        if (isNotTheIp(ip)) {
            if (isNotTheIp(ip)) {
                ip = servletRequest.getHeader("Proxy-Client-IP");
            }
            if (isNotTheIp(ip)) {
                ip = servletRequest.getHeader("WL-Proxy-Client-IP");
            }
            if (isNotTheIp(ip)) {
                ip = servletRequest.getHeader("HTTP_CLIENT_IP");
            }
            if (isNotTheIp(ip)) {
                ip = servletRequest.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (isNotTheIp(ip)) {
                ip = servletRequest.getRemoteAddr();
            }
        }
        // 处理多个IP的情况
        for (String s : ip.split(",")) {
            if (!("unknown".equalsIgnoreCase(s))) {
                return s;
            }
        }
        return ip;
    }

    /**
     * IP是否可用
     *
     * @param ip
     *            IP
     */
    private boolean isNotTheIp(String ip) {
        return ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip);
    }
}

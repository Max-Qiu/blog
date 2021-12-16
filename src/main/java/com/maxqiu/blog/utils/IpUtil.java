package com.maxqiu.blog.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

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
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    /**
     * 根据浏览器标识判断是否为爬虫
     *
     * @param userAgent
     *            浏览器标识
     */
    public boolean isSpider(String userAgent) {
        return userAgent.contains("spider") || userAgent.contains("robot");
    }

    /**
     * 是否为云服务商
     *
     * @param operator
     *            运营商
     */
    public boolean operatorIsCloud(String operator) {
        return "微软云".equals(operator) || "阿里云".equals(operator) || "腾讯云".equals(operator) || "谷歌云".equals(operator)
            || "亚马逊云".equals(operator) || "OVH".equals(operator);
    }
}

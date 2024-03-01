package com.maxqiu.blog.service;

import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.stereotype.Service;

import com.maxqiu.blog.pojo.dto.IpInfoDTO;

import jakarta.annotation.Resource;

/**
 * IP地址信息缓存 服务类
 *
 * @author Max_Qiu
 */
@Service
public class IpInfoService {
    @Resource
    private Ipv4InfoService ipv4InfoService;

    @Resource
    private Ipv6InfoService ipv6InfoService;

    /**
     * 根据IP地址字符串获取IP信息
     *
     * @param ipStr
     *            IP字符串
     */
    public synchronized IpInfoDTO getByIpStr(String ipStr) {
        if (InetAddressUtils.isIPv4Address(ipStr)) {
            return ipv4InfoService.getByStr(ipStr);
        } else if (InetAddressUtils.isIPv6Address(ipStr)) {
            return ipv6InfoService.getByStr(ipStr);
        } else {
            return null;
        }
    }
}

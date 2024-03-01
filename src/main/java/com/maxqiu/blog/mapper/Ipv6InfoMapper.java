package com.maxqiu.blog.mapper;

import com.maxqiu.blog.entity.Ipv6Info;

/**
 * IP地址信息缓存 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface Ipv6InfoMapper {
    Ipv6Info getByIpStr(String ipStr);

    void insert(Ipv6Info newIpInfo);

    void updateById(Ipv6Info newIpInfo);
}

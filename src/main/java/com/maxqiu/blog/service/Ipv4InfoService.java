package com.maxqiu.blog.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.Ipv4Info;
import com.maxqiu.blog.mapper.Ipv4InfoMapper;
import com.maxqiu.blog.pojo.dto.Ip138InfoDTO;
import com.maxqiu.blog.pojo.dto.IpInfoDTO;

import cn.hutool.core.net.NetUtil;
import jakarta.annotation.Resource;

/**
 * IP地址信息缓存 服务类
 *
 * @author Max_Qiu
 */
@Service
public class Ipv4InfoService extends ServiceImpl<Ipv4InfoMapper, Ipv4Info> {
    @Resource
    private Ip138Service ip138Service;

    /**
     * 根据IP地址字符串获取IP信息（IPv4）
     *
     * @param ipStr
     *            IP字符串
     */
    public IpInfoDTO getByStr(String ipStr) {
        long longIpv4 = NetUtil.ipv4ToLong(ipStr);
        Ipv4Info ipv4Info = getById(longIpv4);
        // 如果IP信息存在且未超过半年（正常IP地址信息不会变动，半年更新一次即可）
        if (ipv4Info != null) {
            Period period = Period.between(ipv4Info.getUpdateTime().toLocalDate(), LocalDate.now());
            if (period.getYears() <= 0 && period.getMonths() <= 6) {
                return new IpInfoDTO(ipv4Info);
            }
        }
        // 数据库中不存在，调用第三方接口查询IP信息
        Ip138InfoDTO dto = ip138Service.query(ipStr);
        if (dto == null) {
            return null;
        }
        // 调用成功，获取信息
        Ipv4Info newIpInfo = new Ipv4Info();
        newIpInfo.setId(longIpv4);
        newIpInfo.setCountry(dto.getCountry());
        newIpInfo.setProvince(dto.getProvince());
        newIpInfo.setCity(dto.getCity());
        newIpInfo.setCounty(dto.getCounty());
        newIpInfo.setOperator(dto.getOperator());
        newIpInfo.setPostalCode(dto.getPostalCode());
        newIpInfo.setAreaCode(dto.getAreaCode());
        // 根据原IP地址是否在库中或者是否过期进行更新或者保存
        if (ipv4Info != null) {
            // 即使内容不变，也更新时间
            newIpInfo.setUpdateTime(LocalDateTime.now());
            newIpInfo.updateById();
        } else {
            newIpInfo.insert();
        }
        return new IpInfoDTO(newIpInfo);
    }
}

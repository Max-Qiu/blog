package com.maxqiu.blog.pojo.dto;

import com.maxqiu.blog.entity.Ipv4Info;
import com.maxqiu.blog.entity.Ipv6Info;

import cn.hutool.core.net.NetUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * IP地址信息
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class IpInfoDTO {
    /**
     * IP类型
     */
    private Integer type;

    /**
     * 字符串
     */
    private String str;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份/自治区/直辖市
     */
    private String province;

    /**
     * 地级市
     */
    private String city;

    /**
     * 区/县
     */
    private String county;

    /**
     * 运营商
     */
    private String operator;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 地区区号
     */
    private String areaCode;

    public IpInfoDTO(Ipv4Info info) {
        this.setType(4);
        this.setStr(NetUtil.longToIpv4(info.getId()));
        this.setCountry(info.getCountry());
        this.setProvince(info.getProvince());
        this.setCity(info.getCity());
        this.setCounty(info.getCounty());
        this.setOperator(info.getOperator());
        this.setPostalCode(info.getPostalCode());
        this.setAreaCode(info.getAreaCode());
    }

    public IpInfoDTO(Ipv6Info info) {
        this.setType(6);
        this.setStr(info.getStr());
        this.setCountry(info.getCountry());
        this.setProvince(info.getProvince());
        this.setCity(info.getCity());
        this.setCounty(info.getCounty());
        this.setOperator(info.getOperator());
        this.setPostalCode(info.getPostalCode());
        this.setAreaCode(info.getAreaCode());
    }
}

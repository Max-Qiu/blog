package com.maxqiu.blog.pojo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * IP地址信息缓存
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Ip138InfoDTO {
    /**
     * 字符串（即IPV4字符串）
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
}

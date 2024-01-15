package com.maxqiu.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 屏蔽访问类型
 *
 * @author Max_Qiu
 */
@AllArgsConstructor
@Getter
public enum BlockViewTypeEnum {
    USER_AGENT(1, "浏览器标识"),

    IP_OPERATOR(2, "运营商")

    ;

    @EnumValue
    private final Integer code;
    private final String msg;
}

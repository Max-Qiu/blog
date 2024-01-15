package com.maxqiu.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 屏蔽访问条件
 *
 * @author Max_Qiu
 */
@AllArgsConstructor
@Getter
public enum BlockViewConditionEnum {
    EQ(1, "等于"),

    LIKE(2, "匹配")

    ;

    @EnumValue
    private final Integer code;
    private final String msg;
}

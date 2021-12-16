package com.maxqiu.blog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 邮箱配置
 *
 * @author Max_Qiu
 */
@Component
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class EmailProperties {
    /**
     * 是否启用
     */
    private Boolean enable = false;

    /**
     * 管理员邮箱
     */
    private String adminMail;
}

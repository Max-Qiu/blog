package com.maxqiu.blog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * ip138 配置
 *
 * @author Max_Qiu
 */
@Component
@ConfigurationProperties(prefix = "ip138")
@Data
public class Ip138Properties {
    /**
     * 是否启用
     */
    private Boolean enable = false;

    /**
     * token
     */
    private String token;
}

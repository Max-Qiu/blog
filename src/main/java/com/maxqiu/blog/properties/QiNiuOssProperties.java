package com.maxqiu.blog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 七牛云配置
 *
 * @author Max_Qiu
 */
@Component
@ConfigurationProperties(prefix = "qi-niu.oss")
@Data
public class QiNiuOssProperties {
    /**
     * 是否启用
     */
    private Boolean enable = false;

    /**
     * access-key
     */
    private String accessKey;

    /**
     * secret-key
     */
    private String secretKey;

    /**
     * bucket
     */
    private String bucket;
}

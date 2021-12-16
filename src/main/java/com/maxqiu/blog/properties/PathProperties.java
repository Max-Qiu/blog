package com.maxqiu.blog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 路径配置
 *
 * @author Max_Qiu
 */
@Component
@ConfigurationProperties(prefix = "path")
@Getter
@Setter
@NoArgsConstructor
public class PathProperties {
    /**
     * 文件上传本地绝对路径
     */
    private String uploadLocalPath;

    /**
     * web访问静态文件的路径前缀
     */
    private String uploadViewPath;

    /**
     * 静态文件加速地址
     */
    private String cdnPath;

    /**
     * 域名（用于生成sitemap）
     */
    private String hostName;
}

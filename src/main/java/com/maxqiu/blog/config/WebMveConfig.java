package com.maxqiu.blog.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.maxqiu.blog.interceptor.FrontInterceptor;

/**
 * Web配置
 *
 * @author Max_Qiu
 */
@Configuration
public class WebMveConfig implements WebMvcConfigurer {
    @Resource
    private FrontInterceptor frontInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] frontExcludes = new String[] {"/_admin/**", "/assets/**", "/favicon.ico", "/article/addView/**", "/article/addDiscuss/**"};
        registry.addInterceptor(frontInterceptor).addPathPatterns("/**").excludePathPatterns(frontExcludes);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 普通HTML直接渲染
        registry.addViewController("about/web").setViewName("about/web");
        registry.addViewController("about/history").setViewName("about/history");
        registry.addViewController("about/me").setViewName("about/me");
        registry.addViewController("_admin").setViewName("_admin/index");
        registry.addViewController("_admin/login").setViewName("_admin/login");
        registry.addViewController("_admin/discuss/manager").setViewName("_admin/discuss/discussManager");
        registry.addViewController("_admin/label/manager").setViewName("_admin/label/labelManager");
    }
}

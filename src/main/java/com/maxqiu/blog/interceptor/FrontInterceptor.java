package com.maxqiu.blog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.maxqiu.blog.service.ArticleService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 前端展示拦截器，用于显示文章总量，浏览总量
 *
 * @author Max_Qiu
 */
@Component
public class FrontInterceptor implements HandlerInterceptor {
    @Resource
    private ArticleService articleService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("countShow", articleService.countShow());
            modelAndView.addObject("countView", articleService.countView());
        }
    }
}

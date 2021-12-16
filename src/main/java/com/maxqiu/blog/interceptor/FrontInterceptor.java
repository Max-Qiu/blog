package com.maxqiu.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.maxqiu.blog.service.ArticleService;

/**
 * 前端展示拦截器，用于显示文章总量，浏览总量
 *
 * @author Max_Qiu
 */
@Component
public class FrontInterceptor implements HandlerInterceptor {
    @Autowired
    private ArticleService articleService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("countShow", articleService.countShow());
            modelAndView.addObject("countView", articleService.countView());
        }
    }
}

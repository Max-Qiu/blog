package com.maxqiu.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.maxqiu.blog.common.SystemConstant;

/**
 * 后台管理员验证拦截器
 *
 * @author Max_Qiu
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        // 从session中获取当前登录人
        Integer userId = (Integer)request.getSession().getAttribute(SystemConstant.USER_ID_IN_SESSION_KEY);
        // 如果用户存在且是超级管理员
        if (userId != null && userId == 1) {
            return true;
        }
        // 其他情况，重定向至登录页面
        response.sendRedirect("/_admin/login");
        return false;
    }
}

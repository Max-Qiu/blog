package com.maxqiu.blog.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maxqiu.blog.entity.User;
import com.maxqiu.blog.service.TotpService;
import com.maxqiu.blog.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TOTP 验证过滤器
 * 
 * 在表单登录之前验证 TOTP 动态验证码
 *
 * @author Max_Qiu
 */
public class TotpVerificationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final TotpService totpService;
    private final AuthenticationFailureHandler failureHandler;

    public TotpVerificationFilter(UserService userService, TotpService totpService) {
        this.userService = userService;
        this.totpService = totpService;
        // 使用默认的失败处理器，失败后重定向到登录页面并显示错误信息
        this.failureHandler = new SimpleUrlAuthenticationFailureHandler("/_admin/login?error");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // 只处理登录请求（POST 方法，路径为 /_admin/login）
        if (!isLoginRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. 获取用户名
            String username = request.getParameter("username");
            if (username == null || username.isEmpty()) {
                throw new BadCredentialsException("用户名不能为空");
            }

            // 2. 查询用户信息
            User user = userService.getByUsername(username);
            if (user == null) {
                // 用户不存在，继续后续的认证流程（让 Spring Security 处理）
                filterChain.doFilter(request, response);
                return;
            }

            // 3. 检查用户是否设置了 TOTP
            String totpSecret = user.getTotpSecret();
            if (totpSecret == null || totpSecret.isEmpty()) {
                // 未设置 TOTP，不允许登录
                throw new BadCredentialsException("该账号未设置动态验证码，请联系管理员配置");
            }

            // 4. 用户设置了 TOTP，需要验证动态验证码
            String totpCode = request.getParameter("totpCode");
            if (totpCode == null || totpCode.isEmpty()) {
                throw new BadCredentialsException("请输入动态验证码");
            }

            // 5. 验证 TOTP 验证码
            if (!totpService.verifyCode(totpSecret, totpCode)) {
                throw new BadCredentialsException("动态验证码错误");
            }

            // 6. TOTP 验证通过，继续后续的认证流程（用户名密码验证）
            filterChain.doFilter(request, response);

        } catch (AuthenticationException e) {
            // 验证失败，使用失败处理器处理
            failureHandler.onAuthenticationFailure(request, response, e);
        }
    }

    /**
     * 判断是否是登录请求
     *
     * @param request HTTP 请求
     * @return 是否是登录请求
     */
    private boolean isLoginRequest(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod()) && "/_admin/login".equals(request.getRequestURI());
    }

}

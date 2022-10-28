package com.maxqiu.blog.config;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import com.maxqiu.blog.common.SystemConstant;
import com.maxqiu.blog.entity.User;
import com.maxqiu.blog.service.LogLoginService;
import com.maxqiu.blog.service.UserService;
import com.maxqiu.blog.utils.IpUtil;

/**
 * @author Max_Qiu
 */
@Configuration
public class SecurityConfig {
    @Resource
    private UserService userService;

    @Resource
    private LogLoginService logLoginService;

    @Resource
    private IpUtil ipUtil;

    @Resource
    private DataSource dataSource;

    /**
     * 用户详细信息
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.getByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名不存在！");
            }
            List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList("admin");
            // 这里创建的用户是org.springframework.security.core.userdetails.User
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), auths);
        };
    }

    /**
     * 指定密码的加密方式，这样在密码的前面就不需要添加{bcrypt}
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义安全配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 开启基于表单的身份验证
        http.formLogin(formLoginConfigurer -> {
            // 自定义登录页面
            formLoginConfigurer.loginPage("/_admin/login");
            // 设置登录成功后的处理器以及页面
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler() {
                /**
                 * 重写 SavedRequestAwareAuthenticationSuccessHandler ，添加登录成功后的一些操作
                 */
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws ServletException, IOException {
                    String name = authentication.getName();
                    User user = userService.getByUsername(name);
                    request.getSession().setAttribute(SystemConstant.USER_ID_IN_SESSION_KEY, user.getId());
                    logLoginService.add(user.getId(), ipUtil.getIpAddress(request));
                    Cookie cookie = new Cookie("admin", "true");
                    cookie.setMaxAge(365 * 24 * 60 * 60);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            };
            successHandler.setDefaultTargetUrl("/_admin");
            formLoginConfigurer.successHandler(successHandler);
        });

        http.logout(logoutConfigurer -> {
            // 指定退出登录页
            logoutConfigurer.logoutUrl("/_admin/logout");
        });

        // 启用记住我
        http.rememberMe(rememberMeConfigurer -> {
            // 设置token时间（单位：秒）
            rememberMeConfigurer.tokenValiditySeconds(7 * 24 * 60 * 60);

            // 启用数据库存储登录持久化数据
            JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
            jdbcTokenRepository.setDataSource(dataSource);
            rememberMeConfigurer.tokenRepository(jdbcTokenRepository);
        });

        // 配置认证
        http.authorizeRequests(urlRegistry -> {
            // 指定管理员登录页面无需权限校验
            urlRegistry.antMatchers("/_admin/login").permitAll();
            // 所有的其他管理员页面请求都需要认证
            urlRegistry.antMatchers("/_admin/**").authenticated();
        });

        // 配置 csrf （直接关闭配置）
        http.csrf(CsrfConfigurer::disable);

        return http.build();
    }
}

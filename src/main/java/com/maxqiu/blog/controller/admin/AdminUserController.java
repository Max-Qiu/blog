package com.maxqiu.blog.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.common.SystemConstant;
import com.maxqiu.blog.request.AdminLoginPageRequest;
import com.maxqiu.blog.service.LogLoginService;
import com.maxqiu.blog.service.UserService;
import com.maxqiu.blog.utils.IpUtil;

/**
 * 用户 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("_admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LogLoginService logLoginService;

    @Autowired
    private IpUtil ipUtil;

    /**
     * 登录（管理员）
     */
    @PostMapping("login")
    public Result<String> login(HttpServletRequest servletRequest, HttpSession session,
        @Validated AdminLoginPageRequest request) {
        Integer userId = userService.managerLogin(request.getUsername(), request.getPassword());
        if (userId == null) {
            return Result.fail();
        }
        session.setAttribute(SystemConstant.USER_ID_IN_SESSION_KEY, userId);
        logLoginService.add(userId, ipUtil.getIpAddress(servletRequest));
        return Result.success();
    }

    /**
     * 登录（管理员）
     */
    @PostMapping("logout")
    public Result<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }
}

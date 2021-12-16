package com.maxqiu.blog.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.User;
import com.maxqiu.blog.mapper.UserMapper;

import cn.hutool.crypto.digest.DigestUtil;

/**
 * 用户 服务类
 *
 * @author Max_Qiu
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    /**
     * 管理员登录
     *
     * @param username
     *            用户名
     * @param password
     *            密码
     */
    public Integer managerLogin(String username, String password) {
        username = username.trim();
        password = password.trim();
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        User user = getOne(wrapper);
        // 用户不存在
        if (user == null) {
            return null;
        }
        // 未通过密码验证
        if (!DigestUtil.md5Hex(password).equals(user.getPassword())) {
            return null;
        }
        return user.getId();
    }
}

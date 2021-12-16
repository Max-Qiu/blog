package com.maxqiu.blog.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.LogLogin;
import com.maxqiu.blog.mapper.LogLoginMapper;

import cn.hutool.core.net.NetUtil;

/**
 * 登录日志 服务类
 *
 * @author Max_Qiu
 */
@Service
public class LogLoginService extends ServiceImpl<LogLoginMapper, LogLogin> {
    /**
     * 添加登录日志
     *
     * @param userId
     *            用户ID
     * @param ipStr
     *            IP地址（字符串）
     */
    public boolean add(Integer userId, String ipStr) {
        LogLogin logLogin = new LogLogin();
        logLogin.setUserId(userId);
        logLogin.setIp(NetUtil.ipv4ToLong(ipStr));
        return logLogin.insert();
    }
}

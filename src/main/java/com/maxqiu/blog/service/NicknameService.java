package com.maxqiu.blog.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.Nickname;
import com.maxqiu.blog.mapper.NicknameMapper;

/**
 * 随机昵称 服务类
 *
 * @author Max_Qiu
 */
@Service
public class NicknameService extends ServiceImpl<NicknameMapper, Nickname> {

}

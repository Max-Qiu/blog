package com.maxqiu.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.LogArticle;
import com.maxqiu.blog.mapper.LogArticleMapper;

import cn.hutool.core.net.NetUtil;

/**
 * 文章访问日志 服务类
 *
 * @author Max_Qiu
 */
@Service
public class LogArticleService extends ServiceImpl<LogArticleMapper, LogArticle> {
    /**
     * 检查日志是否存在
     *
     * @param articleId
     *            文章ID
     * @param cookie
     *            用户标识
     */
    public boolean checkHasLog(Integer articleId, String cookie) {
        // 判断是否已浏览过
        LambdaQueryWrapper<LogArticle> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LogArticle::getArticleId, articleId);
        wrapper.eq(LogArticle::getCookie, cookie);
        List<LogArticle> list = list(wrapper);
        return list.size() > 0;
    }

    /**
     * 添加日志
     *
     * @param articleId
     *            文章ID
     * @param mark
     *            用户标识
     * @param referer
     *            来源
     * @param userAgent
     *            浏览器标识
     * @param ipStr
     *            IP（String字符串）
     */
    public void add(Integer articleId, String mark, String referer, String userAgent, String ipStr) {
        LogArticle logArticle = new LogArticle();
        logArticle.setArticleId(articleId);
        logArticle.setCookie(mark);
        logArticle.setUserAgent(userAgent);
        if (StringUtils.hasText(referer)) {
            logArticle.setReferer(referer);
        }
        logArticle.setIp(NetUtil.ipv4ToLong(ipStr));
        logArticle.insert();
    }
}

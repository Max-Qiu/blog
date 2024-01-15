package com.maxqiu.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.BlockView;
import com.maxqiu.blog.entity.IpInfo;
import com.maxqiu.blog.entity.LogArticle;
import com.maxqiu.blog.enums.BlockViewConditionEnum;
import com.maxqiu.blog.enums.BlockViewTypeEnum;
import com.maxqiu.blog.mapper.LogArticleMapper;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;

/**
 * 文章访问日志 服务类
 *
 * @author Max_Qiu
 */
@Service
public class LogArticleService extends ServiceImpl<LogArticleMapper, LogArticle> {
    @Resource
    private BlockViewService blockViewService;

    @Resource
    private IpInfoService ipInfoService;

    /**
     * 检查是否需要屏蔽
     *
     * @return 屏蔽规则ID，如果为空，则不屏蔽
     */
    public Integer checkNeedBlock(String userAgent, String ipStr) {
        // 获取所有的屏蔽规则
        List<BlockView> blockViewList = blockViewService.listByPriority();

        // 获取IP信息
        IpInfo ipInfo = ipInfoService.getByIpStr(ipStr);

        for (BlockView blockView : blockViewList) {
            // 按浏览器标识屏蔽
            if (BlockViewTypeEnum.USER_AGENT.equals(blockView.getType())) {
                if (condition(blockView.getCondition(), userAgent, blockView.getValue())) {
                    return blockView.getId();
                }
            }
            if (BlockViewTypeEnum.IP_OPERATOR.equals(blockView.getType()) && StrUtil.isNotBlank(ipInfo.getOperator())) {
                if (condition(blockView.getCondition(), ipInfo.getOperator(), blockView.getValue())) {
                    return blockView.getId();
                }
            }
        }

        return null;
    }

    private boolean condition(BlockViewConditionEnum conditionEnum, String key, String value) {
        if (conditionEnum.equals(BlockViewConditionEnum.EQ)) {
            if (key.equals(value)) {
                return true;
            }
        }
        if (conditionEnum.equals(BlockViewConditionEnum.LIKE)) {
            return key.contains(value);
        }
        return false;
    }

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
     * @param blockId
     *            屏蔽ID
     */
    public void add(Integer articleId, String mark, String referer, String userAgent, String ipStr, Integer blockId) {
        LogArticle logArticle = new LogArticle();
        logArticle.setArticleId(articleId);
        logArticle.setCookie(mark);
        logArticle.setUserAgent(userAgent);
        if (StringUtils.hasText(referer)) {
            logArticle.setReferer(referer);
        }
        logArticle.setIp(NetUtil.ipv4ToLong(ipStr));
        if (blockId == null) {
            logArticle.setBlocked(false);
        } else {
            logArticle.setBlocked(true);
            logArticle.setBlockId(blockId);
        }
        logArticle.insert();
    }
}

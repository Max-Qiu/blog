package com.maxqiu.blog.service;

import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.BlockView;
import com.maxqiu.blog.entity.LogArticle;
import com.maxqiu.blog.enums.BlockViewConditionEnum;
import com.maxqiu.blog.enums.BlockViewTypeEnum;
import com.maxqiu.blog.mapper.LogArticleMapper;
import com.maxqiu.blog.pojo.dto.IpInfoDTO;

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
        List<BlockView> blockViewList = blockViewService.listOrderByCount();

        // 获取IP信息
        IpInfoDTO dto = ipInfoService.getByIpStr(ipStr);

        for (BlockView blockView : blockViewList) {
            // 按浏览器标识屏蔽
            if (BlockViewTypeEnum.USER_AGENT.equals(blockView.getType())) {
                if (condition(blockView.getCondition(), userAgent, blockView.getValue())) {
                    return blockView.getId();
                }
            } else if (BlockViewTypeEnum.IP_OPERATOR.equals(blockView.getType()) && dto != null && StrUtil.isNotBlank(dto.getOperator())) {
                if (condition(blockView.getCondition(), dto.getOperator(), blockView.getValue())) {
                    return blockView.getId();
                }
            }
        }

        return null;
    }

    private boolean condition(BlockViewConditionEnum conditionEnum, String key, String value) {
        if (conditionEnum.equals(BlockViewConditionEnum.EQ)) {
            return key.equals(value);
        } else if (conditionEnum.equals(BlockViewConditionEnum.LIKE)) {
            return key.contains(value);
        }
        return false;
    }

    /**
     * 统计日志数量
     *
     * @param articleId
     *            文章ID
     * @param mark
     *            用户标识
     */
    public long count(Integer articleId, String mark) {
        LambdaQueryWrapper<LogArticle> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LogArticle::getArticleId, articleId);
        wrapper.eq(LogArticle::getMark, mark);
        return count(wrapper);
    }

    /**
     * 添加日志
     *
     * @param articleId
     *            文章ID
     * @param mark
     *            用户标识
     * @param userAgent
     *            浏览器标识
     * @param referer
     *            来源
     * @param ipStr
     *            IP（String字符串）
     * @param blockId
     *            屏蔽ID
     */
    public void add(Integer articleId, String mark, String userAgent, String referer, String ipStr, Integer blockId) {
        baseMapper.add(articleId, mark, userAgent, referer, InetAddressUtils.isIPv4Address(ipStr) ? 4 : InetAddressUtils.isIPv6Address(ipStr) ? 6 : 0,
            ipStr, blockId);
    }
}

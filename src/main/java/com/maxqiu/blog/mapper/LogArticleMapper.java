package com.maxqiu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.blog.entity.LogArticle;

/**
 * 文章访问日志 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface LogArticleMapper extends BaseMapper<LogArticle> {
    void add(Integer articleId, String mark, String userAgent, String referer, Integer ipType, String ipStr, Integer blockId);
}

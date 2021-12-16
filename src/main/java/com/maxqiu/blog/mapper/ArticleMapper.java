package com.maxqiu.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.blog.entity.Article;

/**
 * 文章 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 文章展示总量
     */
    Long countShow();

    /**
     * 文章流量总量
     */
    Long countView();

    /**
     * 置顶与热门的10个文章
     */
    List<Article> top();

    /**
     * 分页查询
     *
     * @param page
     *            分页对象
     * @param labelId
     *            标签ID
     */
    Page<Article> pageQuery(@Param("page") Page<Article> page, @Param("labelId") Integer labelId);

    /**
     * 分页查询（管理端）
     *
     * @param page
     *            分页对象
     * @param title
     *            标题
     * @param labelId
     *            标签ID
     * @param top
     *            置顶状态
     * @param show
     *            展示状态
     */
    Page<Article> managerPageQuery(@Param("page") Page<Article> page, @Param("title") String title,
        @Param("labelId") Integer labelId, @Param("top") Boolean top, @Param("show") Boolean show);

    /**
     * 新增浏览器
     *
     * @param articleId
     *            文章ID
     */
    boolean addView(@Param("articleId") Integer articleId);

    /**
     * 刷新文章的标签名称
     */
    void flushName();
}

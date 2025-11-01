package com.maxqiu.blog.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.mapper.ArticleMapper;

import cn.hutool.http.HtmlUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章 服务类
 *
 * @author Max_Qiu
 */
@Slf4j
@CacheConfig(cacheNames = "blog")
@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    @Resource
    private LabelService labelService;

    /**
     * 文章展示总量
     */
    @Cacheable(key = "'countShow'")
    public Long countShow() {
        return baseMapper.countShow();
    }

    /**
     * 文章流量总量
     */
    @Cacheable(key = "'countView'")
    public Long countView() {
        return baseMapper.countView();
    }

    /**
     * 置顶与热门的10个文章
     */
    public List<Article> top() {
        return baseMapper.top();
    }

    /**
     * 分页查询
     *
     * @param pageNumber
     *            页码
     * @param pageSize
     *            页面大小
     * @param labelId
     *            标签ID
     */
    public Page<Article> pageQuery(Integer pageNumber, Integer pageSize, Integer labelId) {
        return baseMapper.pageQuery(new Page<>(pageNumber, pageSize), labelId);
    }

    /**
     * 分页查询（管理端）
     *
     * @param pageNumber
     *            页码
     * @param pageSize
     *            分页大小
     * @param title
     *            标题
     * @param labelId
     *            标签ID
     * @param top
     *            是否置顶
     * @param show
     *            是否展示
     */
    public Page<Article> managerPageQuery(Integer pageNumber, Integer pageSize, String title, Integer labelId, Boolean top, Boolean show) {
        return baseMapper.managerPageQuery(new Page<>(pageNumber, pageSize), title, labelId, top, show);
    }

    /**
     * 文章保存或更新
     *
     * @param id
     *            文章ID
     * @param labelIds
     *            标签
     * @param title
     *            标题
     * @param md
     *            源码
     * @param html
     *            HTML
     */
    public Integer form(Integer id, String labelIds, String title, String md, String html) {
        Article article = new Article();
        article.setLabelIds(labelIds);
        article.setTitle(title);
        article.setMd(md);
        article.setText(HtmlUtil.cleanHtmlTag(html));
        article.setLabelNames(labelService.getLabelNamesByLabelIds(article.getLabelIds()));
        boolean flag;
        if (id != null) {
            article.setId(id);
            article.setModifiedTime(LocalDateTime.now());
            flag = article.updateById();
        } else {
            article.setView(0);
            article.setTop(false);
            article.setShow(false);
            flag = article.insert();
        }
        if (flag) {
            labelService.flushNum();
            return article.getId();
        } else {
            log.error("文章保存失败：{}", article);
            return null;
        }
    }

    /**
     * 修改是否置顶
     *
     * @param articleId
     *            文章ID
     * @param top
     *            置顶状态
     */
    public boolean changeTop(Integer articleId, Boolean top) {
        Article article = new Article();
        article.setId(articleId);
        article.setTop(top);
        return article.updateById();
    }

    /**
     * 修改状态
     *
     * @param articleId
     *            文章ID
     * @param show
     *            展示状态
     */
    @Caching(evict = {@CacheEvict(key = "'countShow'", beforeInvocation = true), @CacheEvict(key = "'countView'", beforeInvocation = true)})
    public boolean changeShow(Integer articleId, Boolean show) {
        Article article = new Article();
        article.setId(articleId);
        article.setShow(show);
        boolean flag = article.updateById();
        if (flag) {
            // 刷新标签数量
            labelService.flushNum();
        } else {
            log.error("文章更新显示失败：{}", articleId);
        }
        return flag;
    }

    /**
     * 新增浏览量
     *
     * @param articleId
     *            文章ID
     */
    @CacheEvict(key = "'countView'")
    public void addView(Integer articleId) {
        baseMapper.addView(articleId);
    }

    /**
     * 查找所有的展示的文章信息（用户初始化sitemap文件）
     */
    public List<Article> allShowInfo() {
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Article::getId, Article::getModifiedTime);
        wrapper.eq(Article::getShow, 1);
        return list(wrapper);
    }

    /**
     * 检查文章内容中是否包含该文件（用于清理文件）
     *
     * @param fileName
     *            文件名称
     */
    public boolean checkHasFilename(String fileName) {
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery();
        wrapper.like(Article::getMd, fileName);
        return count(wrapper) > 0;
    }

    /**
     * 刷新文章的标签名称
     */
    public void flushName() {
        baseMapper.flushName();
    }

}

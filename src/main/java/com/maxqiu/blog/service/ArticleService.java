package com.maxqiu.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.entity.ArticleEs;
import com.maxqiu.blog.mapper.ArticleMapper;
import com.maxqiu.blog.repository.ArticleRepository;

import cn.hutool.http.HtmlUtil;
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
    @Autowired
    private LabelService labelService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchRestTemplate template;

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
     * 搜索
     *
     * @param pageNumber
     *            页码
     * @param pageSize
     *            分页大小
     * @param labelId
     *            标签ID
     * @param search
     *            搜索词
     */
    public SearchHits<ArticleEs> search(Integer pageNumber, Integer pageSize, Integer labelId, String search) {
        // 子选项，标题和文章选中一个即可
        BoolQueryBuilder childBoolQueryBuilder = QueryBuilders.boolQuery();
        // 标题匹配，可选
        childBoolQueryBuilder.should(QueryBuilders.matchQuery("title", search));
        // 文章匹配，可选
        childBoolQueryBuilder.should(QueryBuilders.matchQuery("text", search));

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 标题和文章必须选中一个
        boolQueryBuilder.must(childBoolQueryBuilder);
        // 标签匹配，如果有，则必须
        if (labelId != 0) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("labelIds", labelId));
        }

        // 标签匹配，如果有，则必须
        boolQueryBuilder.must(QueryBuilders.matchQuery("show", true));

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
            // 分页
            .withPageable(PageRequest.of(pageNumber - 1, pageSize))
            // 标题、文本高亮
            .withHighlightFields(new HighlightBuilder.Field("text").fragmentSize(250).preTags("<span style='color: #e9c984;'>").postTags("</span>"),
                new HighlightBuilder.Field("title").preTags("<span style='color: #e9c984;'>").postTags("</span>"))
            // 查询条件， 必须要有条件，否则无法高亮
            .withQuery(boolQueryBuilder);
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        return template.search(query, ArticleEs.class);
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
            flag = article.updateById();
        } else {
            article.setView(0);
            article.setTop(false);
            article.setShow(false);
            flag = article.insert();
        }
        if (flag) {
            labelService.flushNum();
            articleRepository.save(new ArticleEs(getById(article.getId())));
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
        boolean flag = article.updateById();
        if (flag) {
            // 从数据库取出并重新保存数据
            articleRepository.save(new ArticleEs(getById(articleId)));
        } else {
            log.error("文章更新置顶失败：{}", articleId);
        }
        return flag;
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
            // 修改ES中的文章状态，从数据库取出并重新保存数据
            articleRepository.save(new ArticleEs(getById(articleId)));
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
        boolean flag = baseMapper.addView(articleId);
        if (flag) {
            // 从数据库取出并重新保存数据
            articleRepository.save(new ArticleEs(getById(articleId)));
        } else {
            log.error("文章新增浏览失败：{}", articleId);
        }
    }

    /**
     * 查找所有的展示的文章ID（用户初始化sitemap文件）
     */
    public List<Integer> allShowId() {
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Article::getId);
        wrapper.eq(Article::getShow, 1);
        return list(wrapper).stream().map(Article::getId).collect(Collectors.toList());
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
        flushAllToEs();
    }

    /**
     * 刷新所有文章至ES
     */
    public void flushAllToEs() {
        List<ArticleEs> collect = list().stream().map(ArticleEs::new).collect(Collectors.toList());
        articleRepository.saveAll(collect);
    }
}

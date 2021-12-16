package com.maxqiu.blog.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.maxqiu.blog.entity.ArticleEs;

/**
 * 文章 elasticsearch 数据操作接口
 *
 * @author Max_Qiu
 */
public interface ArticleRepository extends ElasticsearchRepository<ArticleEs, Integer> {

}

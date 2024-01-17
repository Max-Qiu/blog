package com.maxqiu.blog.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 文章
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "blog_article")
@Setting(shards = 1, replicas = 0)
public class ArticleEs {
    /**
     * 文章主键ID
     */
    @Id
    private Integer id;

    /**
     * 标签ID
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String labelIds;

    /**
     * 标签名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String labelNames;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    /**
     * 纯文字
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String text;

    /**
     * 浏览
     */
    @Field(type = FieldType.Integer)
    private Integer view;

    /**
     * 置顶 0否 1是
     */
    @Field(type = FieldType.Boolean)
    private Boolean top;

    /**
     * 展示 0否 1是
     */
    @Field(type = FieldType.Boolean)
    private Boolean show;

    /**
     * 创建日期
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createTime;

    public ArticleEs(Article article) {
        setId(article.getId());
        setLabelIds(article.getLabelIds());
        setLabelNames(article.getLabelNames());
        setTitle(article.getTitle());
        setText(article.getText());
        setView(article.getView());
        setTop(article.getTop());
        setShow(article.getShow());
        setCreateTime(article.getCreateTime());
    }
}

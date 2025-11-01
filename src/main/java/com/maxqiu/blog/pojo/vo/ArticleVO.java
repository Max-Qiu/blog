package com.maxqiu.blog.pojo.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxqiu.blog.entity.Article;

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
public class ArticleVO {

    /**
     * 文章主键ID
     */
    private Integer id;

    /**
     * 标签ID
     */
    private String labelIds;

    /**
     * 标签名称
     */
    private String labelNames;

    /**
     * 文章标题
     */
    private String title;

    /**
     * md源码
     */
    private String md;

    /**
     * 纯文字
     */
    private String text;

    /**
     * 浏览
     */
    private Integer view;

    /**
     * 置顶 0否 1是
     */
    private Boolean top;

    /**
     * 展示 0否 1是
     */
    private Boolean show;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @JsonFormat(pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;

    public ArticleVO(Article article) {
        this.id = article.getId();
        this.labelIds = article.getLabelIds();
        this.labelNames = article.getLabelNames();
        this.title = article.getTitle();
        this.md = article.getMd();
        this.text = article.getText();
        this.view = article.getView();
        this.top = article.getTop();
        this.show = article.getShow();
        this.createTime = article.getCreateTime();
        this.modifiedTime = article.getModifiedTime();
    }

}

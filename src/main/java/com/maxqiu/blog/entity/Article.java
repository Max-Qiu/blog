package com.maxqiu.blog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 文章
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("article")
public class Article extends Model<Article> {
    private static final long serialVersionUID = 1L;

    /**
     * 文章主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签ID
     */
    @TableField("label_ids")
    private String labelIds;

    /**
     * 标签名称
     */
    @TableField("label_names")
    private String labelNames;

    /**
     * 文章标题
     */
    @TableField("title")
    private String title;

    /**
     * md源码
     */
    @TableField("md")
    private String md;

    /**
     * 纯文字
     */
    @TableField("`text`")
    private String text;

    /**
     * 浏览
     */
    @TableField("`view`")
    private Integer view;

    /**
     * 置顶 0否 1是
     */
    @TableField("top")
    private Boolean top;

    /**
     * 展示 0否 1是
     */
    @TableField("`show`")
    private Boolean show;

    /**
     * 创建日期
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

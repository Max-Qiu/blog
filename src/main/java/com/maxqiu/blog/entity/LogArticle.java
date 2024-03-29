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
 * 文章访问日志
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("log_article")
public class LogArticle extends Model<LogArticle> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章ID
     */
    @TableField("article_id")
    private Integer articleId;

    /**
     * 用户标记
     */
    @TableField("mark")
    private String mark;

    /**
     * 浏览器标识
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 来源
     */
    @TableField("referer")
    private String referer;

    /**
     * 屏蔽 0否 1是
     */
    @TableField("blocked")
    private Boolean blocked;

    /**
     * 屏蔽原因ID
     */
    @TableField("block_id")
    private Integer blockId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

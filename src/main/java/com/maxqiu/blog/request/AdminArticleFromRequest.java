package com.maxqiu.blog.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 文章管理 - 新增文章
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AdminArticleFromRequest {
    /**
     * 文章主键ID
     */
    private Integer id;

    /**
     * 标签ID
     */
    @NotBlank
    private String labelIds;

    /**
     * 文章标题
     */
    @NotBlank
    private String title;

    /**
     * md源码
     */
    @NotBlank
    private String md;

    /**
     * HTML
     */
    @NotBlank
    private String html;
}

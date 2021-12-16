package com.maxqiu.blog.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 文章管理 - 分页
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminArticlePageRequest extends AbstractPageRequest {
    /**
     * 标题
     */
    private String title;

    /**
     * 标签ID
     */
    private Integer labelId;

    /**
     * 是否置顶
     */
    private Boolean top;

    /**
     * 是否展示
     */
    private Boolean show;
}

package com.maxqiu.blog.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户端 - 文章搜索
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class FrontArticleSearchRequest extends AbstractPageRequest {
    /**
     * 标签ID
     */
    private Integer labelId = 0;

    /**
     * 搜索词
     */
    private String search;
}

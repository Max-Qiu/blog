package com.maxqiu.blog.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 前端添加浏览器请求
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FrontAddViewFromRequest {
    /**
     * 文章ID
     */
    @NotNull
    private Integer articleId;

    /**
     * 来源
     */
    private String referer;
}

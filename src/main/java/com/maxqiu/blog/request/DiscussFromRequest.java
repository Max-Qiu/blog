package com.maxqiu.blog.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 提交评论（用户端 + 管理端）
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class DiscussFromRequest {
    /**
     * 文章ID
     */
    @NotNull
    private Integer articleId;

    /**
     * 用户昵称
     */
    @NotBlank
    private String nickname;

    /**
     * 评论内容
     */
    @NotBlank
    private String content;

    /**
     * 回复的用户ID
     */
    @NotNull
    private Integer revertId;
}

package com.maxqiu.blog.pojo.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxqiu.blog.entity.Discuss;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 评论
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class DiscussVO {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 文章ID
     */
    private Integer articleId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 层级
     */
    private Integer tier;

    /**
     * 回复的用户ID
     */
    private Integer revertId;

    /**
     * 是否审核过的 0否 1是
     */
    private Boolean check;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 子评论
     */
    private List<DiscussVO> childList;

    public DiscussVO(Discuss discuss) {
        this.id = discuss.getId();
        this.articleId = discuss.getArticleId();
        this.nickname = discuss.getNickname();
        this.content = discuss.getContent();
        this.tier = discuss.getTier();
        this.revertId = discuss.getRevertId();
        this.check = discuss.getCheck();
        this.createTime = discuss.getCreateTime();
    }

    public DiscussVO(Discuss discuss, List<DiscussVO> childList) {
        this.id = discuss.getId();
        this.articleId = discuss.getArticleId();
        this.nickname = discuss.getNickname();
        this.content = discuss.getContent();
        this.tier = discuss.getTier();
        this.revertId = discuss.getRevertId();
        this.check = discuss.getCheck();
        this.createTime = discuss.getCreateTime();
        this.childList = childList;
    }
}

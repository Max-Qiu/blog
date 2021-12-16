package com.maxqiu.blog.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 评论管理 - 分页
 *
 * @author Max_Qiu
 */
@Getter
@Setter
public class AdminDiscussPageRequest extends AbstractPageRequest {
    /**
     * 是否审核
     */
    private Boolean check;
}

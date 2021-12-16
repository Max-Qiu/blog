package com.maxqiu.blog.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 标签管理 - 分页
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class AdminLabelPageRequest extends AbstractPageRequest {
    private String name;
}

package com.maxqiu.blog.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 公用分页数据
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractPageRequest {
    /**
     * 页码
     */
    private Integer pageNumber = 1;

    /**
     * 页面大小
     */
    private Integer pageSize = 10;
}

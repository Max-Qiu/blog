package com.maxqiu.blog.pojo.vo;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 前端分页数据VO
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class PageResultVO<T> {
    /**
     * 对象集合
     */
    private List<T> list;

    /**
     * 页码
     */
    private Long pageNumber;

    /**
     * 页面大小
     */
    private Long pageSize;

    /**
     * 总行数
     */
    private Long totalRow;

    public PageResultVO(List<T> list, Long pageNumber, Long pageSize, Long totalRow) {
        this.list = list;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRow = totalRow;
    }
}

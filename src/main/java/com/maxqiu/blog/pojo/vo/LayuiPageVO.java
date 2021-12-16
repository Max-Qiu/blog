package com.maxqiu.blog.pojo.vo;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 管理端LayUI分页数据返回对象
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class LayuiPageVO<T> {
    private Integer code;
    private String msg;
    private Long count;
    private List<T> data;

    public LayuiPageVO(Long count, List<T> data) {
        this.code = 0;
        this.msg = "";
        this.count = count;
        this.data = data;
    }

    public LayuiPageVO(Page<T> page) {
        this.code = 0;
        this.msg = "";
        this.count = page.getTotal();
        this.data = page.getRecords();
    }
}

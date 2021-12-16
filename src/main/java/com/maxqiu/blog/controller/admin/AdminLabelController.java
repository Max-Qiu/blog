package com.maxqiu.blog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.entity.Label;
import com.maxqiu.blog.pojo.vo.LayuiPageVO;
import com.maxqiu.blog.request.AdminLabelFromRequest;
import com.maxqiu.blog.request.AdminLabelPageRequest;
import com.maxqiu.blog.service.ArticleService;
import com.maxqiu.blog.service.LabelService;

/**
 * 标签 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("_admin/label")
public class AdminLabelController {
    @Autowired
    private LabelService labelService;

    @Autowired
    private ArticleService articleService;

    /**
     * 管理端分页展示
     */
    @PostMapping("page")
    public LayuiPageVO<Label> page(AdminLabelPageRequest request) {
        Page<Label> page = labelService.pageQuery(request.getPageNumber(), request.getPageSize(), request.getName());
        return new LayuiPageVO<>(page);
    }

    /**
     * 表单提交
     */
    @PostMapping("form")
    public Result<Integer> form(@Validated AdminLabelFromRequest request) {
        Label label = new Label();
        label.setId(request.getId());
        label.setName(request.getName());
        boolean flag;
        if (label.getId() == null) {
            label.setCount(0);
            flag = labelService.save(label);
        } else {
            flag = labelService.updateById(label);
            articleService.flushName();
        }
        if (flag) {
            return Result.success(label.getId());
        } else {
            return Result.fail();
        }
    }
}

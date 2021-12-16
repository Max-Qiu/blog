package com.maxqiu.blog.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.entity.Discuss;
import com.maxqiu.blog.pojo.vo.DiscussVO;
import com.maxqiu.blog.pojo.vo.LayuiPageVO;
import com.maxqiu.blog.request.AdminDiscussPageRequest;
import com.maxqiu.blog.request.DiscussFromRequest;
import com.maxqiu.blog.service.ArticleService;
import com.maxqiu.blog.service.DiscussService;

/**
 * 评论管理
 *
 * @author Max_Qiu
 */
@Controller
@RequestMapping("_admin/discuss")
public class AdminDiscussController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private DiscussService discussService;

    /**
     * 评论分页数据（管理端）
     */
    @PostMapping("page")
    @ResponseBody
    public LayuiPageVO<DiscussVO> page(AdminDiscussPageRequest request) {
        Page<Discuss> discussPage =
            discussService.managerPage(request.getPageNumber(), request.getPageSize(), request.getCheck());
        List<DiscussVO> voList = discussPage.getRecords().stream().map(DiscussVO::new).collect(Collectors.toList());
        return new LayuiPageVO<>(discussPage.getTotal(), voList);
    }

    /**
     * 单个文章的详细评论
     *
     * @param articleId
     *            文章ID
     */
    @GetMapping("detail")
    public String detail(Model model, Integer articleId) {
        model.addAttribute("article", articleService.getById(articleId));
        model.addAttribute("discussList", discussService.list(articleId, true));
        return "_admin/discuss/discussDetail";
    }

    /**
     * 新增评论（管理员）
     */
    @PostMapping("form")
    @ResponseBody
    public Result<String> form(@Validated DiscussFromRequest request) {
        return Result.byFlag(discussService.form(request.getArticleId(), request.getNickname(), request.getContent(),
            request.getRevertId()));
    }

    /**
     * 删除评论
     *
     * @param discussIds
     *            评论ID列表
     */
    @PostMapping("del")
    @ResponseBody
    public Result<String> discussDel(String discussIds) {
        List<Integer> discussIdList =
            Arrays.stream(discussIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        discussService.deleteByIds(discussIdList);
        return Result.success();
    }

    /**
     * 变更评论审核状态
     *
     * @param discussId
     *            评论ID
     * @param check
     *            是否审核
     */
    @PostMapping("changeCheck")
    @ResponseBody
    public Result<String> changeCheck(Integer discussId, Boolean check) {
        discussService.changeCheck(discussId, check);
        return Result.success();
    }
}

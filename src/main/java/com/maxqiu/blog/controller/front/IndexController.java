package com.maxqiu.blog.controller.front;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.service.ArticleService;

/**
 * 首页
 *
 * @author Max_Qiu
 */
@Controller
@RequestMapping("")
public class IndexController {
    @Resource
    private ArticleService articleService;

    /**
     * 首页
     */
    @GetMapping("")
    public String index(Model model) {
        List<Article> list = articleService.top();
        model.addAttribute("list", list);
        return "index";
    }
}

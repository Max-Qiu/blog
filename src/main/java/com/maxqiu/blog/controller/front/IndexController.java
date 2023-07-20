package com.maxqiu.blog.controller.front;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.service.ArticleService;

import jakarta.annotation.Resource;

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
        LocalDateTime time = LocalDateTime.now();
        model.addAttribute("day", time.getHour() + "/23");
        LocalDate date = time.toLocalDate();
        model.addAttribute("week", date.getDayOfWeek().getValue() + "/7");
        model.addAttribute("month", date.getMonthValue() + "/12");
        model.addAttribute("year", date.getDayOfYear() + "/" + (date.isLeapYear() ? 366 : 365));
        return "index";
    }
}

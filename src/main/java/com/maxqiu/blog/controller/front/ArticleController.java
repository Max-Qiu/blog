package com.maxqiu.blog.controller.front;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.common.UserConstant;
import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.entity.Label;
import com.maxqiu.blog.pojo.vo.ArticleVO;
import com.maxqiu.blog.pojo.vo.PageResultVO;
import com.maxqiu.blog.request.DiscussFromRequest;
import com.maxqiu.blog.request.FrontArticleSearchRequest;
import com.maxqiu.blog.service.ArticleService;
import com.maxqiu.blog.service.DiscussService;
import com.maxqiu.blog.service.EmailService;
import com.maxqiu.blog.service.LabelService;
import com.maxqiu.blog.service.LogArticleService;
import com.maxqiu.blog.utils.IpUtil;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 文章
 *
 * @author Max_Qiu
 */
@Controller
@RequestMapping("article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private DiscussService discussService;

    @Resource
    private LabelService labelService;

    @Resource
    private LogArticleService logArticleService;

    @Resource
    private IpUtil ipUtil;

    @Resource
    private EmailService emailService;

    /**
     * 列表显示
     */
    @GetMapping("")
    public String page(Model model, FrontArticleSearchRequest request) {
        PageResultVO<ArticleVO> resultVO;

        Page<Article> page = articleService.pageQuery(request.getPageNumber(), request.getPageSize(), request.getLabelId());
        List<ArticleVO> collect = page.getRecords().stream().map(ArticleVO::new).collect(Collectors.toList());
        resultVO = new PageResultVO<>(collect, page.getCurrent(), page.getSize(), page.getTotal());
        model.addAttribute("page", resultVO);

        // 显示标签列表
        List<Label> labelList = labelService.listAndDescByNum();
        model.addAttribute("labelList", labelList.stream().filter(e -> e.getCount() != 0).toList());

        model.addAttribute("labelId", request.getLabelId());

        return "article/articleList";
    }

    /**
     * 详细数据
     *
     * @param articleId
     *            文章ID
     */
    @GetMapping("/detail/{articleId}")
    public String detail(Model model, @RequestHeader("User-Agent") String userAgent,
        @RequestHeader(value = "referer", required = false) String referer, @CookieValue(value = "nickname", defaultValue = "") String nickname,
        @CookieValue(value = "mark", defaultValue = "") String mark, HttpSession session, HttpServletRequest servletRequest,
        HttpServletResponse servletResponse, @PathVariable Integer articleId) {
        // === 判断文章后面是否有参数，如果有，则301跳转 ===
        String queryString = servletRequest.getQueryString();
        if (queryString != null) {
            servletRequest.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);
            servletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            return "redirect:" + servletRequest.getRequestURI();
        }

        // === 获取文章详细内容，并判断文章状态 ===
        Article article = articleService.getById(articleId);
        // 文章不存在，返回404
        if (article == null) {
            servletResponse.setStatus(404);
            return null;
        }
        boolean isAdmin = "admin".equals(SecurityContextHolder.getContext().getAuthentication().getName());
        // 文章是否可以查看
        if (!article.getShow()) {
            // 判断用户
            if (!isAdmin) {
                servletResponse.setStatus(404);
                return null;
            }
        }
        model.addAttribute("article", article);

        // === 获取评论内容 ===
        model.addAttribute("discussList", discussService.list(articleId, false));

        // === 设置昵称 ===
        if (ObjectUtils.isEmpty(nickname)) {
            nickname = (String)session.getAttribute("nickname");
            if (ObjectUtils.isEmpty(nickname)) {
                nickname = isAdmin ? "管理员" : UserConstant.getRandomNickname();
                session.setAttribute("nickname", nickname);
            }
            Cookie cookie = new Cookie("nickname", URLEncoder.encode(nickname, StandardCharsets.UTF_8));
            cookie.setMaxAge(315360000);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);
        } else {
            nickname = URLDecoder.decode(nickname, StandardCharsets.UTF_8);
            if (ObjectUtils.isEmpty(session.getAttribute("nickname"))) {
                session.setAttribute("nickname", nickname);
            }
        }
        model.addAttribute("nickname", nickname);

        // === 设置用户标识 ===
        if (ObjectUtils.isEmpty(mark)) {
            mark = (String)session.getAttribute("mark");
            if (ObjectUtils.isEmpty(mark)) {
                mark = isAdmin ? "admin" : UUID.randomUUID().toString();
                session.setAttribute("mark", mark);
            }
            Cookie cookie = new Cookie("mark", mark);
            cookie.setMaxAge(315360000);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);
        } else {
            if (ObjectUtils.isEmpty(session.getAttribute("mark"))) {
                session.setAttribute("mark", mark);
            }
        }

        // === 记录日志 ===
        if (isAdmin) {
            // 管理员仅显示评论列表，不记录访问日志
            model.addAttribute("showDiscuss", true);
        } else {
            // 获取用户IP
            String ipStr = ipUtil.getIpAddress(servletRequest);
            // 检查浏览器标识和IP是否被屏蔽
            Integer blockId = logArticleService.checkNeedBlock(userAgent, ipStr);
            // 如果不需要屏蔽，则显示评论
            model.addAttribute("showDiscuss", blockId == null);
            // 添加日志
            logArticleService.add(articleId, mark, userAgent, referer, ipStr, blockId);
            // 添加浏览量（未屏蔽的访问且第一次访问）
            long count = logArticleService.count(articleId, mark);
            if (count <= 1 && blockId == null) {
                articleService.addView(articleId);
                article.setView(article.getView() + 1);
            }
        }

        return "article/articleDetail";
    }

    /**
     * 新增评论（用户端）
     */
    @PostMapping("addDiscuss")
    @ResponseBody
    public Result<String> addDiscuss(@Validated DiscussFromRequest request) {
        emailService.sendRemindToAdmin(request.getArticleId(), request.getContent());
        return Result.byFlag(discussService.form(request.getArticleId(), request.getNickname(), request.getContent(), request.getRevertId()));
    }

}

package com.maxqiu.blog.controller.front;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.common.UserConstant;
import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.entity.ArticleEs;
import com.maxqiu.blog.entity.Label;
import com.maxqiu.blog.enums.ResultEnum;
import com.maxqiu.blog.pojo.vo.ArticleVO;
import com.maxqiu.blog.pojo.vo.PageResultVO;
import com.maxqiu.blog.request.DiscussFromRequest;
import com.maxqiu.blog.request.FrontAddViewFromRequest;
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
        if (StringUtils.hasText(request.getSearch())) {
            SearchHits<ArticleEs> searchHits =
                articleService.search(request.getPageNumber(), request.getPageSize(), request.getLabelId(), request.getSearch());
            List<SearchHit<ArticleEs>> searchHitList = searchHits.getSearchHits();
            List<ArticleVO> collect = searchHitList.stream().map(item -> {
                ArticleEs article = item.getContent();
                ArticleVO vo = new ArticleVO(article);
                // 处理高亮结果
                Map<String, List<String>> highlightFields = item.getHighlightFields();
                // 判断是否有高亮标题
                List<String> title = highlightFields.get("title");
                if (title == null) {
                    vo.setTitle(article.getTitle());
                } else {
                    vo.setTitle(title.get(0));
                }
                // 是否有高亮文本
                List<String> text = highlightFields.get("text");
                if (text == null) {
                    vo.setText("");
                } else {
                    String s = text.toString();
                    s = s.substring(1, s.length() - 1);
                    s = s.replace(", ", "<br><br>");
                    vo.setText(s);
                }
                return vo;
            }).collect(Collectors.toList());
            resultVO = new PageResultVO<>(collect, request.getPageNumber().longValue(), request.getPageSize().longValue(), searchHits.getTotalHits());
        } else {
            Page<Article> page = articleService.pageQuery(request.getPageNumber(), request.getPageSize(), request.getLabelId());
            List<ArticleVO> collect = page.getRecords().stream().map(ArticleVO::new).collect(Collectors.toList());
            resultVO = new PageResultVO<>(collect, page.getCurrent(), page.getSize(), page.getTotal());
        }
        model.addAttribute("page", resultVO);

        // 显示标签列表
        List<Label> labelList = labelService.listAndDescByNum();
        model.addAttribute("labelList", labelList.stream().filter(e -> e.getCount() != 0).toList());

        // 数据回显
        if (StringUtils.hasText(request.getSearch())) {
            model.addAttribute("search", request.getSearch());
        }
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
    public String detail(Model model, @RequestHeader(value = "referer", required = false) String referer,
        @CookieValue(value = "nickname", defaultValue = "") String nickname, @CookieValue(value = "mark", defaultValue = "") String mark,
        HttpSession session, HttpServletResponse servletResponse, @PathVariable Integer articleId) {
        // === 获取文章详细内容，并判断文章状态 ===
        Article article = articleService.getById(articleId);
        // 文章不存在，返回404
        if (article == null) {
            servletResponse.setStatus(404);
            return null;
        }
        // 文章是否可以查看
        if (!article.getShow()) {
            // 判断用户
            if (SecurityContextHolder.getContext().getAuthentication() == null
                || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                || !"admin".equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
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
                nickname = UserConstant.getRandomNickname();
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
                mark = UUID.randomUUID().toString();
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

        // === 设置来源 ===
        model.addAttribute("referer", referer);

        return "article/articleDetail";
    }

    /**
     * 添加浏览量
     */
    @PostMapping("addView")
    @ResponseBody
    public Result<Integer> addView(@RequestHeader("User-Agent") String userAgent, HttpServletRequest servletRequest, HttpSession session,
        @RequestBody FrontAddViewFromRequest fromRequest) {
        // 判断SESSION中的用户昵称和用户标识
        String mark = (String)session.getAttribute("mark");
        String nickname = (String)session.getAttribute("nickname");
        if (ObjectUtils.isEmpty(mark) || ObjectUtils.isEmpty(nickname)) {
            // 有任意一个为空，非正常访问，直接返回
            return Result.error();
        }
        // 用户标识存在，且已访问过
        boolean hasLog = logArticleService.checkHasLog(fromRequest.getArticleId(), mark);
        if (hasLog) {
            return Result.other(ResultEnum.EXIST_LOG);
        }

        // 获取用户IP
        String ipStr = ipUtil.getIpAddress(servletRequest);

        // 检查浏览器标识和IP是否被屏蔽
        Integer blockId = logArticleService.checkNeedBlock(userAgent, ipStr);

        // 添加日志
        logArticleService.add(fromRequest.getArticleId(), mark, fromRequest.getReferer(), userAgent, ipStr, blockId);
        // 添加浏览量
        if (blockId == null) {
            articleService.addView(fromRequest.getArticleId());
            return Result.success();
        }
        return Result.fail();
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

package com.maxqiu.blog.controller.front;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.common.SystemConstant;
import com.maxqiu.blog.common.UserConstant;
import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.entity.ArticleEs;
import com.maxqiu.blog.entity.IpInfo;
import com.maxqiu.blog.entity.Label;
import com.maxqiu.blog.pojo.vo.ArticleVO;
import com.maxqiu.blog.pojo.vo.PageResultVO;
import com.maxqiu.blog.request.DiscussFromRequest;
import com.maxqiu.blog.request.FrontArticleSearchRequest;
import com.maxqiu.blog.service.ArticleService;
import com.maxqiu.blog.service.DiscussService;
import com.maxqiu.blog.service.EmailService;
import com.maxqiu.blog.service.IpInfoService;
import com.maxqiu.blog.service.LabelService;
import com.maxqiu.blog.service.LogArticleService;
import com.maxqiu.blog.utils.IpUtil;

/**
 * 文章
 *
 * @author Max_Qiu
 */
@Controller
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private DiscussService discussService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private LogArticleService logArticleService;

    @Autowired
    private IpInfoService ipInfoService;

    @Autowired
    private IpUtil ipUtil;

    @Autowired
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
        List<Label> labelList = labelService.listByNum();
        model.addAttribute("labelList", labelList);

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
    public String detail(Model model, @RequestHeader("user-agent") String userAgent,
        @RequestHeader(value = "referer", required = false) String referer, @CookieValue(value = "nickname", defaultValue = "") String nickname,
        @CookieValue(value = "mark", defaultValue = "") String mark, @CookieValue(value = "admin", defaultValue = "") String admin,
        HttpSession session, HttpServletRequest servletRequest, HttpServletResponse servletResponse, @PathVariable Integer articleId) {
        // === 获取文章详细内容，并判断文章状态 ===
        Article article = articleService.getById(articleId);
        // 文章不存在，返回404
        if (article == null) {
            servletResponse.setStatus(404);
            return null;
        }
        // 文章是否可以查看
        if (!article.getShow()) {
            // 文章状态时隐藏时，获取当前用户
            Integer user = (Integer)session.getAttribute(SystemConstant.USER_ID_IN_SESSION_KEY);
            // 如果当前用户为空，或者当前用户不是管理员，则返回404
            if (user == null || user != 1) {
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
            cookie.setMaxAge(SystemConstant.COOKIE_AGE);
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
            cookie.setMaxAge(SystemConstant.COOKIE_AGE);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);
        } else {
            if (ObjectUtils.isEmpty(session.getAttribute("mark"))) {
                session.setAttribute("mark", mark);
            }
        }

        // === 记录访问日志 ===
        // 1. 非管理员才记录
        if (!"true".equals(admin)) {
            // 2. cookie 不存在才记录
            Optional<Cookie> optional = servletRequest.getCookies() == null ? Optional.empty()
                : Arrays.stream(servletRequest.getCookies()).filter(e -> ("view" + articleId).equals(e.getName())).findFirst();
            if (optional.isEmpty()) {
                // 3. 不是爬虫才记录
                if (!ipUtil.isSpider(userAgent)) {
                    // 获取用户IP
                    String ipStr = ipUtil.getIpAddress(servletRequest);
                    /// 4. 不是云服务器访问才记录
                    IpInfo ipInfo = ipInfoService.getByIpStr(ipStr);
                    if (ipInfo != null && !ipUtil.operatorIsCloud(ipInfo.getOperator())) {
                        // 查询是否已存在记录
                        boolean hasLog = logArticleService.checkHasLog(articleId, mark);
                        // 5. 不存浏览记录
                        if (!hasLog) {
                            // 添加浏览量
                            articleService.addView(articleId);
                            // 添加日志
                            logArticleService.add(articleId, mark, referer, userAgent, ipStr);
                        }
                        Cookie cookie = new Cookie("view" + articleId, "" + articleId);
                        cookie.setMaxAge(SystemConstant.COOKIE_AGE);
                        cookie.setPath("/");
                        servletResponse.addCookie(cookie);
                    }
                }
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

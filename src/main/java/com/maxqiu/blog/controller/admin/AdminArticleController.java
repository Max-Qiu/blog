package com.maxqiu.blog.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.pojo.vo.ArticleVO;
import com.maxqiu.blog.pojo.vo.LayuiPageVO;
import com.maxqiu.blog.properties.PathProperties;
import com.maxqiu.blog.request.AdminArticleFromRequest;
import com.maxqiu.blog.request.AdminArticlePageRequest;
import com.maxqiu.blog.service.ArticleService;
import com.maxqiu.blog.service.LabelService;
import com.maxqiu.blog.service.QiNiuOssService;

/**
 * 文章管理
 *
 * @author Max_Qiu
 */
@Controller
@RequestMapping("_admin/article")
public class AdminArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private PathProperties pathProperties;

    @Autowired
    private QiNiuOssService qiNiuOssService;

    /**
     * 页面展示
     */
    @GetMapping("manager")
    public String manager(Model model) {
        model.addAttribute("labelList", labelService.listByNum());
        return "_admin/article/articleManager";
    }

    /**
     * 分页查询（管理端）
     */
    @PostMapping("page")
    @ResponseBody
    public LayuiPageVO<ArticleVO> page(AdminArticlePageRequest request) {
        Page<Article> page = articleService.managerPageQuery(request.getPageNumber(), request.getPageSize(),
            request.getTitle(), request.getLabelId(), request.getTop(), request.getShow());
        List<ArticleVO> voList = page.getRecords().stream().map(ArticleVO::new).collect(Collectors.toList());
        return new LayuiPageVO<>(page.getTotal(), voList);
    }

    /**
     * 文章编辑
     *
     * @param articleId
     *            文章ID
     */
    @RequestMapping("edit")
    public String edit(Model model, Integer articleId) {
        model.addAttribute("labelList", labelService.listByNum());
        // 如果ID不存在，则为新建
        if (articleId != null) {
            Article article = articleService.getById(articleId);
            List<Integer> hasLabelList =
                Arrays.stream(article.getLabelIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());
            model.addAttribute("hasLabelList", hasLabelList);
            model.addAttribute("article", article);
        } else {
            model.addAttribute("hasLabelList", new ArrayList<>());
            model.addAttribute("article", new ArticleVO());
        }
        return "_admin/article/articleEdit";
    }

    /**
     * 文章编辑的表单提交
     */
    @PostMapping("form")
    @ResponseBody
    public Result<Integer> articleEditForm(@Validated AdminArticleFromRequest request) {
        Integer id = articleService.form(request.getId(), request.getLabelIds(), request.getTitle(), request.getMd(),
            request.getHtml());
        if (id != null) {
            return Result.success(id);
        } else {
            return Result.error();
        }
    }

    /**
     * Markdown编辑的图片上传
     */
    @PostMapping("mdImageUpload")
    @ResponseBody
    public Map<String, Object> mdImgUpload(@RequestParam("editormd-image-file") MultipartFile multipartFile) {
        Map<String, Object> map = new HashMap<>();
        // 是否包含文件
        if (multipartFile == null || multipartFile.isEmpty()) {
            map.put("success", 0);
            map.put("message", "上传失败，文件不存在");
            return map;
        }
        // 是否可以获取文件名
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            map.put("success", 0);
            map.put("message", "获取文件名称失败");
            return map;
        }
        // 获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成新的文件名称
        String newFileName = UUID.randomUUID().toString().replace("-", "") + fileType;
        // 使用新的文件名创建新文件（绝对路径）
        File newFile = new File(pathProperties.getUploadLocalPath() + newFileName);
        try {
            // 上传的文件拷贝至新文件
            multipartFile.transferTo(newFile);
            // 拷贝完成后上传至云存储
            qiNiuOssService.uploadFile(newFile, pathProperties.getUploadViewPath().substring(1) + newFileName);
            map.put("success", 1);
            map.put("message", "上传成功");
            // 返回文件访问地址：cdn路径 + 浏览路径 + 新文件名
            map.put("url", pathProperties.getCdnPath() + pathProperties.getUploadViewPath() + newFileName);
            return map;
        } catch (IOException e) {
            map.put("success", 0);
            map.put("message", "上传失败" + e);
            return map;
        }
    }

    /**
     * 修改是否置顶
     *
     * @param articleId
     *            文章ID
     * @param top
     *            是否置顶
     */
    @PostMapping("changeTop")
    @ResponseBody
    public Result<String> changeTop(Integer articleId, Boolean top) {
        return Result.byFlag(articleService.changeTop(articleId, top));
    }

    /**
     * 修改状态
     *
     * @param articleId
     *            文章ID
     * @param show
     *            是否展示
     */
    @PostMapping("changeShow")
    @ResponseBody
    public Result<String> changeShow(Integer articleId, Boolean show) {
        return Result.byFlag(articleService.changeShow(articleId, show));
    }
}

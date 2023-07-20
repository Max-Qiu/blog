package com.maxqiu.blog.controller.admin;

import java.io.File;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.blog.common.Result;
import com.maxqiu.blog.enums.ResultEnum;
import com.maxqiu.blog.properties.PathProperties;
import com.maxqiu.blog.service.ArticleService;
import com.maxqiu.blog.service.QiNiuOssService;
import com.maxqiu.blog.utils.SitemapUtil;

import jakarta.annotation.Resource;

/**
 * 首页
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("_admin")
public class AdminIndexController {
    @Resource
    private SitemapUtil sitemapUtil;

    @Resource
    private PathProperties pathProperties;

    @Resource
    private QiNiuOssService qiNiuOssService;

    @Resource
    private ArticleService articleService;

    /**
     * 整理服务器上的文章图片文件
     */
    @PostMapping("clean")
    public Result<Integer> clean() {
        File folder = new File(pathProperties.getUploadLocalPath());
        // 文件夹不存在
        if (!folder.isDirectory()) {
            return Result.other(ResultEnum.NOT_DIRECTORY);
        }
        // 文件内容空
        File[] listFiles = folder.listFiles();
        if (listFiles == null) {
            return Result.other(ResultEnum.NOT_DIRECTORY);
        }
        int count = 0;
        for (File file : listFiles) {
            // 检查文章中是否有该文件
            boolean checkHasFilename = articleService.checkHasFilename(file.getName());
            if (!checkHasFilename) {
                boolean delete = file.delete();
                if (delete) {
                    count++;
                    qiNiuOssService.deleteFile(pathProperties.getUploadViewPath().substring(1) + file.getName());
                }
            }
        }
        return Result.success(count);
    }

    /**
     * 重新生成sitemap
     */
    @PostMapping("sitemap")
    public Result<String> sitemap() {
        return Result.byFlag(sitemapUtil.generateSitemap());
    }
}

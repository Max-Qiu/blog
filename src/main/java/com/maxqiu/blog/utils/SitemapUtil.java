package com.maxqiu.blog.utils;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.maxqiu.blog.entity.Article;
import com.maxqiu.blog.properties.PathProperties;
import com.maxqiu.blog.service.ArticleService;

import cn.hutool.core.util.XmlUtil;
import jakarta.annotation.Resource;

/**
 * @author Max_Qiu
 */
@Component
public class SitemapUtil {
    @Resource
    private ArticleService articleService;

    @Resource
    private PathProperties pathProperties;

    /**
     * 生成sitemap文件
     */
    public boolean generateSitemap() {
        List<Article> idList = articleService.allShowInfo();
        String path;
        try {
            path = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX + "static").getPath() + "/sitemap.xml";
        } catch (FileNotFoundException e) {
            System.out.println("static路径获取异常");
            return false;
        }
        String bashUrl = pathProperties.getHostName() + "/article/detail/";

        Document document = XmlUtil.createXml();
        document.setXmlStandalone(true);

        Element urlset = document.createElement("urlset");
        urlset.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");

        // 向bookstore根节点中添加子节点book

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        for (Article article : idList) {
            Element url = document.createElement("url");
            Element loc = document.createElement("loc");
            loc.setTextContent(bashUrl + article.getId());
            url.appendChild(loc);
            Element lastmod = document.createElement("lastmod");
            lastmod.setTextContent(formatter.format(article.getModifiedTime()));
            url.appendChild(lastmod);
            urlset.appendChild(url);
        }

        document.appendChild(urlset);

        try {
            XmlUtil.toFile(document, path, "utf-8");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

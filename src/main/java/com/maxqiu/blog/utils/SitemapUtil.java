package com.maxqiu.blog.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.maxqiu.blog.properties.PathProperties;
import com.maxqiu.blog.service.ArticleService;

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
        List<Integer> idList = articleService.allShowId();
        String path;
        try {
            path = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX + "static").getPath();
        } catch (FileNotFoundException e) {
            System.out.println("static路径获取异常");
            return false;
        }
        String bashUrl = pathProperties.getHostName() + "/article/detail/";
        File file = new File(path + "/sitemap.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            for (Integer id : idList) {
                writer.write(bashUrl + id + "\r\n");
            }
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

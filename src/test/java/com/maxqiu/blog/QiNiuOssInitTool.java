package com.maxqiu.blog;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.blog.service.QiNiuOssService;

/**
 * 七牛云文件资源初始化工具
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class QiNiuOssInitTool {
    @Autowired
    private QiNiuOssService qiNiuOssService;

    /**
     * 上传静态资源文件
     */
    @Test
    void flushStatic() {
        String staticPath = System.getProperty("user.dir") + "\\..\\max-blog-web\\src\\main\\resources\\static";
        File assets = new File(staticPath + "\\assets");
        Map<String, Integer> result = qiNiuOssService.uploadDirectory("assets", assets);
        System.out.println("上传成功：" + result.get("success"));
        System.out.println("上传失败：" + result.get("error"));
        boolean b = qiNiuOssService.uploadFile(new File(staticPath + "/favicon.ico"), "favicon.ico");
        System.out.println(b ? "图标上传成功" : "图标上传失败");
    }

    /**
     * 上传文章图片资源
     */
    @Test
    public void flushUpload() {
        File file = new File("C:\\upload");
        Map<String, Integer> result = qiNiuOssService.uploadDirectory("upload", file);
        System.out.println("上传成功：" + result.get("success"));
        System.out.println("上传失败：" + result.get("error"));
    }
}

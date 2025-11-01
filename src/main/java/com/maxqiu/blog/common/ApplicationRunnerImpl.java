package com.maxqiu.blog.common;

import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.maxqiu.blog.entity.Nickname;
import com.maxqiu.blog.service.NicknameService;
import com.maxqiu.blog.utils.SitemapUtil;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动后初始化操作
 *
 * @author Max_Qiu
 */
@Slf4j
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Resource
    private SitemapUtil sitemapUtil;

    @Resource
    private NicknameService nicknameService;

    @Override
    public void run(ApplicationArguments args) {

        // 初始化 sitemap 文件
        boolean generateSitemap = sitemapUtil.generateSitemap();
        if (!generateSitemap) {
            log.error("初始化 sitemap 文件失败！");
        }

        // 初始化随机用户昵称
        UserConstant.nicknameList = nicknameService.list().stream().map(Nickname::getNickname).collect(Collectors.toList());

    }

}

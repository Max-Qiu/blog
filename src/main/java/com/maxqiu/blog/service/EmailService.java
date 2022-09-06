package com.maxqiu.blog.service;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.maxqiu.blog.properties.EmailProperties;
import com.maxqiu.blog.utils.EmailUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Max_Qiu
 */
@Slf4j
@Service
public class EmailService {
    @Resource
    private EmailUtil emailUtil;

    @Resource
    private EmailProperties emailProperties;

    /**
     * 发送邮件至管理员
     *
     * @param articleId
     *            文章ID
     * @param content
     *            内容
     */
    @Async
    public void sendRemindToAdmin(Integer articleId, String content) {
        if (emailProperties.getEnable()) {
            boolean flag = emailUtil.simpleMailMessage(emailProperties.getAdminMail(), "文章ID：" + articleId + " 被评论了", content);
            if (!flag) {
                log.error("邮件发送失败：{} + {}", articleId, content);
            }
        }
    }
}

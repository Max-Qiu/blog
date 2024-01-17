package com.maxqiu.blog.utils;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件 服务
 *
 * @author Max_Qiu
 */
@Component
@Slf4j
public class EmailUtil {
    /**
     * 邮件发送
     */
    @Resource
    private JavaMailSender mailSender;

    /**
     * 发件人
     */
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送简单邮件
     *
     * @param toList
     *            接收人列表
     * @param ccList
     *            抄送人列表
     * @param bccList
     *            密送人列表
     * @param subject
     *            主题
     * @param text
     *            内容
     */
    public boolean simpleMailMessage(List<String> toList, List<String> ccList, List<String> bccList, String subject, String text) {
        // 1. 创建一个简单消息
        SimpleMailMessage message = new SimpleMailMessage();
        // 2. 设置
        // 发件人
        message.setFrom(from);
        // 收件人
        if (toList == null || toList.isEmpty()) {
            return false;
        } else if (toList.size() == 1) {
            message.setTo(toList.get(0));
        } else {
            String[] strings = new String[toList.size()];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = toList.get(i);
            }
            message.setTo(strings);
        }
        // 抄送
        if (ccList != null) {
            if (ccList.size() == 1) {
                message.setCc(ccList.get(0));
            } else if (!ccList.isEmpty()) {
                String[] strings = new String[ccList.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = ccList.get(i);
                }
                message.setCc(strings);
            }
        }
        // 密送
        if (bccList != null) {
            if (bccList.size() == 1) {
                message.setCc(bccList.get(0));
            } else if (!bccList.isEmpty()) {
                String[] strings = new String[bccList.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = bccList.get(i);
                }
                message.setBcc(strings);
            }
        }
        // 主体
        message.setSubject(subject);
        // 内容
        message.setText(text);
        // 3. 发送
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("邮件发送失败", e);
            return false;
        }
        return true;
    }

    /**
     * 发送简单邮件
     *
     * @param toList
     *            接收人列表
     * @param subject
     *            主题
     * @param text
     *            内容
     */
    public boolean simpleMailMessage(List<String> toList, String subject, String text) {
        return simpleMailMessage(toList, null, null, subject, text);
    }

    /**
     * 发送简单邮件
     *
     * @param to
     *            接收人
     * @param subject
     *            主题
     * @param text
     *            内容
     */
    public boolean simpleMailMessage(String to, String subject, String text) {
        return simpleMailMessage(Collections.singletonList(to), subject, text);
    }
}

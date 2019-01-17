package com.gxg.service;

import org.json.JSONObject;

/**
 * 发送邮件相关接口
 * @author 郭欣光
 * @date 2019/1/16 16:17
 */
public interface MailService {

    /**
     * 发送html邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param emailContent 邮件内容
     * @return 发送结果
     * @author 郭欣光
     */
    JSONObject sendHtmlMail(String to, String subject, String emailContent);
}

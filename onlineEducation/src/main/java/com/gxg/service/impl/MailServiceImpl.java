package com.gxg.service.impl;

import com.gxg.service.MailService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件相关处理
 * @author 郭欣光
 * @date 2019/1/16 16:18
 */
@Service(value = "mailService")
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送html邮件
     *
     * @param to           收件人邮箱
     * @param subject      邮件主题
     * @param emailContent 邮件内容
     * @return 发送结果
     * @author 郭欣光
     */
    @Override
    public JSONObject sendHtmlMail(String to, String subject, String emailContent) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发送失败";
        MimeMessage message = sender.createMimeMessage();
        try {
            //true表示需要创建一个mutipart message(多部件消息)
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailContent, true);

            sender.send(message);
            status = "true";
            content = "发送成功！";
        } catch (MessagingException e) {
            content = "发送邮件失败";
            System.out.println(e);
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result;
    }
}

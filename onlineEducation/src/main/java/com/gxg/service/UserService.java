package com.gxg.service;

import com.gxg.entities.User;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户相关处理接口
 * @author 郭欣光
 * @date 2019/1/16 14:44
 */
public interface UserService {

    /**
     * 用户注册信息处理
     * @param email 邮箱
     * @param name 姓名
     * @param password 密码
     * @param repassword 重复密码
     * @param sex 性别
     * @param job 职业
     * @param headImage 头像
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String register(String email, String name, String password, String repassword, String sex, String job, MultipartFile headImage, HttpServletRequest request);

    /**
     * 删除用户
     * @param user 用户信息
     * @return 删除结果
     */
    JSONObject deleteUser(User user);

    /**
     * 用户注册验证
     * @param email 邮箱
     * @param verificationRule 验证规则
     * @return 验证结果
     */
    JSONObject register(String email, String verificationRule);

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @param nextPage 登陆成功后转入页面
     * @param request 用户请求信息
     * @return 登录结果
     * @author 郭欣光
     */
    String login(String email, String password, String nextPage, HttpServletRequest request);

    /**
     * 注销用户
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String cancel(HttpServletRequest request);

    /**
     * 退出登录
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String logout(HttpServletRequest request);

    /**
     * 用户发起重置密码请求，向用户发送重置密码邮箱确认
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String sendResetPasswordEmail(HttpServletRequest request);

    /**
     * 重回密码邮箱验证
     * @param email 邮箱
     * @param rule 验证规则
     * @param request 用户请求信息
     * @return 验证结果
     * @author 郭欣光
     */
    JSONObject resetPasswordVerification(String email, String rule, HttpServletRequest request);

    /**
     * 用户重置密码
     * @param password 密码
     * @param repassword 确认面膜
     * @param request 用户请求信息
     * @return 重置结果
     * @author 郭欣光
     */
    String resetPassword(String password, String repassword, HttpServletRequest request);
}

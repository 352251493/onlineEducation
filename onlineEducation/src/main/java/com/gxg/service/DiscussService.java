package com.gxg.service;

import com.gxg.entities.Discuss;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 讨论帖子相关业务处理接口
 * @author 郭欣光
 * @date 2019/5/17 19:14
 */
public interface DiscussService {

    /**
     * 获取讨论帖子列表
     * @param page 页数
     * @return 讨论帖子相关信息
     * @autjor 郭欣光
     */
    JSONObject getDiscussList(String page);

    /**
     * 创建讨论帖子
     * @param discussName 帖子名称
     * @param discussContent 帖子内容
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createDiscuss(String discussName, String discussContent, HttpServletRequest request);

    /**
     * 根据ID获取讨论帖子信息
     * @param discussId 讨论帖子ID
     * @return 讨论帖子信息
     * @author 郭欣光
     */
    Discuss getDiscussById(String discussId);

    /**
     * 根据用户邮箱获取指定页数的讨论帖子信息
     * @param userEmail 用户邮箱
     * @param discussPage 讨论帖子页数
     * @return 讨论帖子相关信息
     * @author 郭欣光
     */
    JSONObject getDiscussListByUserEmail(String userEmail, String discussPage);

    /**
     * 删除讨论帖子
     * @param discussId 讨论帖子
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String deleteDiscuss(String discussId, HttpServletRequest request);
}

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
}

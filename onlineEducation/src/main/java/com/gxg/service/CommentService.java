package com.gxg.service;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论相关业务处理接口
 * @author 郭欣光
 * @date 2019/5/19 13:46
 */
public interface CommentService {

    /**
     * 获取指定讨论帖子指定页数的评论信息
     * @param discussId 讨论帖子ID
     * @param commentPage 评论页数
     * @return 评论相关信息
     * @author 郭欣光
     */
    JSONObject getCommentListByDiscussId(String discussId, String commentPage);

    /**
     * 创建评论
     * @param discussId 讨论帖子ID
     * @param commentContent 评论内容
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createComment(String discussId, String commentContent, HttpServletRequest request);

    /**
     * 删除评论
     * @param commentId 评论ID
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String deleteComment(String commentId, HttpServletRequest request);
}

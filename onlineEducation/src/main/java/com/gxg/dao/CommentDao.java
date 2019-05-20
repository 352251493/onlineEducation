package com.gxg.dao;

import com.gxg.entities.Comment;

import javax.validation.constraints.AssertFalse;
import java.util.List;

/**
 * 评论相关数据库处理
 * @author 郭欣光
 * @date 2019/5/19 13:56
 */
public interface CommentDao {

    /**
     * 根据讨论帖子ID获取评论个数
     * @param discussId 讨论帖子ID
     * @return 评论个数
     * @author 郭欣光
     */
    int getCountByDiscussId(String discussId);

    /**
     * 根据讨论帖子ID获取按照创建时间排序指定范围的评论信息
     * @param discussId 讨论帖子ID
     * @param limitStart 第一个；limit
     * @param limitEnd 第二个limit
     * @return 评论信息
     * @author 郭欣光
     */
    List<Comment> getCommentByDiscussIdAndLimitOrderByCreateTime(String discussId, int limitStart, int limitEnd);

    /**
     * 根据ID获取评论数量
     * @param id ID
     * @return 评论数量
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 创建评论
     * @param comment 评论相关信息
     * @return 数据库改变个数
     * @author 郭欣光
     */
    int createComment(Comment comment);

    /**
     * 根据ID获取评论信息
     * @param id ID
     * @return 评论信息
     * @author 郭欣光
     */
    Comment getCommentById(String id);

    /**
     * 删除评论
     * @param comment 评论信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteComment(Comment comment);
}

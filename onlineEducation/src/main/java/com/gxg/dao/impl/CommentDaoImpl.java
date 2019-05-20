package com.gxg.dao.impl;

import com.gxg.dao.CommentDao;
import com.gxg.dao.rowmapper.CommentRowMapper;
import com.gxg.entities.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论相关数据库处理
 * @author 郭欣光
 * @date 2019/5/19 13:58
 */
@Repository(value = "commentDao")
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据讨论帖子ID获取评论个数
     *
     * @param discussId 讨论帖子ID
     * @return 评论个数
     * @author 郭欣光
     */
    @Override
    public int getCountByDiscussId(String discussId) {
        String sql = "select count(1) from comment where discuss_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, discussId);
        return rowCount;
    }

    /**
     * 根据讨论帖子ID获取按照创建时间排序指定范围的评论信息
     *
     * @param discussId  讨论帖子ID
     * @param limitStart 第一个；limit
     * @param limitEnd   第二个limit
     * @return 评论信息
     * @author 郭欣光
     */
    @Override
    public List<Comment> getCommentByDiscussIdAndLimitOrderByCreateTime(String discussId, int limitStart, int limitEnd) {
        String sql = "select * from comment where discuss_id=? order by create_time desc limit ?, ?";
        List<Comment> commentList = jdbcTemplate.query(sql, new CommentRowMapper(), discussId, limitStart, limitEnd);
        return commentList;
    }

    /**
     * 根据ID获取评论数量
     *
     * @param id ID
     * @return 评论数量
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from comment where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 创建评论
     *
     * @param comment 评论相关信息
     * @return 数据库改变个数
     * @author 郭欣光
     */
    @Override
    public int createComment(Comment comment) {
        String sql = "insert into comment(id, content, create_time, user_email, discuss_id) values(?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, comment.getId(), comment.getContent(), comment.getCreateTime(), comment.getUserEmail(), comment.getDiscussId());
        return changeCount;
    }

    /**
     * 根据ID获取评论信息
     *
     * @param id ID
     * @return 评论信息
     * @author 郭欣光
     */
    @Override
    public Comment getCommentById(String id) {
        String sql = "select * from comment where id=?";
        Comment comment = jdbcTemplate.queryForObject(sql, new CommentRowMapper(), id);
        return comment;
    }

    /**
     * 删除评论
     *
     * @param comment 评论信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteComment(Comment comment) {
        String sql = "delete from comment where id=?";
        int changeCount = jdbcTemplate.update(sql, comment.getId());
        return changeCount;
    }
}

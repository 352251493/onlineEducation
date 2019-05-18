package com.gxg.dao.impl;

import com.gxg.dao.DiscussDao;
import com.gxg.dao.rowmapper.DiscussRowMapper;
import com.gxg.entities.Discuss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 讨论帖子数据库相关
 * @author 郭欣光
 * @date 2019/5/17 19:23
 */

@Repository(value = "discussDao")
public class DiscussDaoImpl implements DiscussDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取讨论帖子个数
     *
     * @return 讨论帖子个数
     */
    @Override
    public int getCount() {
        String sql = "select count(1) from discuss";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 按照创建时间排序获取指定范围的讨论帖子列表
     *
     * @param limitStart 第一个limit
     * @param limitEnd   第二个limit
     * @return 讨论帖子列表
     * @author 郭欣光
     */
    @Override
    public List<Discuss> getDiscussByLimitOrderByCreateTime(int limitStart, int limitEnd) {
        String sql = "select * from discuss order by create_time desc limit ?, ?";
        List<Discuss> discussList = jdbcTemplate.query(sql, new DiscussRowMapper(), limitStart, limitEnd);
        return discussList;
    }

    /**
     * 根据ID获取讨论帖子个数
     *
     * @param id ID
     * @return 讨论帖子个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from discuss where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 创建讨论帖子
     *
     * @param discuss 讨论帖子
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createDiscuss(Discuss discuss) {
        String sql = "insert into discuss(id, name, content, create_time, user_email) values(?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, discuss.getId(), discuss.getName(), discuss.getContent(), discuss.getCreateTime(), discuss.getUserEmail());
        return changeCount;
    }

    /**
     * 根据ID获取讨论帖子信息
     *
     * @param id ID
     * @return 讨论帖子信息
     * @author 郭欣光
     */
    @Override
    public Discuss getDiscussById(String id) {
        String sql = "select * from discuss where id=?";
        Discuss discuss = jdbcTemplate.queryForObject(sql, new DiscussRowMapper(), id);
        return discuss;
    }
}

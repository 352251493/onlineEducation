package com.gxg.dao.rowmapper;

import com.gxg.entities.Discuss;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2019/5/17 19:37
 */
public class DiscussRowMapper implements RowMapper<Discuss> {

    @Override
    public Discuss mapRow(ResultSet resultSet, int i) throws SQLException {
        Discuss discuss = new Discuss();
        discuss.setId(resultSet.getString("id"));
        discuss.setName(resultSet.getString("name"));
        discuss.setContent(resultSet.getString("content"));
        discuss.setCreateTime(resultSet.getTimestamp("create_time"));
        discuss.setUserEmail(resultSet.getString("user_email"));
        return discuss;
    }
}

package com.gxg.dao.impl;

import com.gxg.dao.LessonDataDao;
import com.gxg.dao.rowmapper.LessonDataRowMapper;
import com.gxg.entities.LessonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2019/2/28 10:57
 */

@Repository(value = "lessonDataDao")
public class LessonDataDaoImpl implements LessonDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int getCountByLessonId(String lessonId) {
        String sql = "select count(1) from lesson_data where lesson_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, lessonId);
        return rowCount;
    }

    /**
     * 根据课时资料ID获取课时资料个数
     *
     * @param id 课时资料ID
     * @return 课时资料个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from lesson_data where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 创建课时资料
     *
     * @param lessonData 课时资料
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createLessonData(LessonData lessonData) {
        String sql = "insert into lesson_data(id, name, path, create_time, lesson_id) value(?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, lessonData.getId(), lessonData.getName(), lessonData.getPath(), lessonData.getCreateTime(), lessonData.getLessonId());
        return changeCount;
    }

    /**
     * 根据课时ID获取课时资料
     *
     * @param lessonId 课时ID
     * @return 课时资料列表
     */
    @Override
    public List<LessonData> getLessonDataByLessonId(String lessonId) {
        String sql = "select * from lesson_data where lesson_id=? order by create_time desc";
        List<LessonData> lessonDataList = jdbcTemplate.query(sql, new LessonDataRowMapper(), lessonId);
        return lessonDataList;
    }

    /**
     * 根据课时资料ID获取课时资料信息
     *
     * @param id 课时资料ID
     * @return 课时资料信息
     * @author 郭欣光
     */
    @Override
    public LessonData getLessonDataById(String id) {
        String sql = "select * from lesson_data where id=?";
        LessonData lessonData = jdbcTemplate.queryForObject(sql, new LessonDataRowMapper(), id);
        return lessonData;
    }

    /**
     * 删除课时资料
     *
     * @param lessonData 课时资料
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteLessonData(LessonData lessonData) {
        String sql = "delete from lesson_data where id=?";
        int changeCount = jdbcTemplate.update(sql, lessonData.getId());
        return changeCount;
    }
}

package com.gxg.dao;

import com.gxg.entities.LessonData;

/**
 * 课程资料数据库先关接口
 * @author 郭欣光
 * @date 2019/2/28 10:56
 */
public interface LessonDataDao {

    int getCountByLessonId(String lessonId);

    /**
     * 根据课时资料ID获取课时资料个数
     * @param id 课时资料ID
     * @return 课时资料个数
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 创建课时资料
     * @param lessonData 课时资料
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int createLessonData(LessonData lessonData);
}

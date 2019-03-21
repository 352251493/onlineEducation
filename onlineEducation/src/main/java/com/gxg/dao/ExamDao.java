package com.gxg.dao;

import com.gxg.entities.Exam;

/**
 * 考试相关数据库信息
 * @author 郭欣光
 * @date 2019/3/21 11:21
 */
public interface ExamDao {

    /**
     * 通过ID获取考试数量
     * @param id ID
     * @return 考试数量
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加考试
     * @param exam 考试信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int createExam(Exam exam);
}

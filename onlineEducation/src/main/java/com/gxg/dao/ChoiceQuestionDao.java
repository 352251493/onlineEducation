package com.gxg.dao;

import com.gxg.entities.ChoiceQuestion;

import java.util.List;

/**
 * 选择题相关数据库接口
 * @author 郭欣光
 * @date 2019/4/4 16:06
 */

public interface ChoiceQuestionDao {

    /**
     * 根据ID获取选择题个数
     * @param id ID
     * @return 选择题个数
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加选择题
     * @param choiceQuestion 选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int addChoiceQuestion(ChoiceQuestion choiceQuestion);

    /**
     * 根据考试ID获取选择题数量
     * @param examId 考试ID
     * @return 选择题数量
     * @author 郭欣光
     */
    int getCountByExamId(String examId);

    /**
     * 根据考试ID获取选择题信息
     * @param examId 考试ID
     * @return 选择题信息
     * @author 郭欣光
     */
    List<ChoiceQuestion> getChoiceQuestionByExamId(String examId);

    /**
     * 根据选择题ID获取选择题信息
     * @param id 选择题ID
     * @return 选择题相关信息
     * @author 郭欣光
     */
    ChoiceQuestion getChoiceQuestionById(String id);

    /**
     * 删除选择题信息
     * @param choiceQuestion  选择题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteChoiceQuestion(ChoiceQuestion choiceQuestion);
}

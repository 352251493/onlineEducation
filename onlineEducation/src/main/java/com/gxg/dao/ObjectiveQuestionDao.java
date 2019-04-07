package com.gxg.dao;

import com.gxg.entities.ObjectiveQuestion;

import java.util.List;

/**
 * 问答题数据库相关接口
 * @author 郭欣光
 * @date 2019/4/5 18:25
 */
public interface ObjectiveQuestionDao {

    /**
     * 根据ID获取问答题数量
     * @param id ID
     * @return 问答题数量
     * @author 郭欣光
     */
    int getCountById(String id);

    /**
     * 添加问答题
     * @param objectiveQuestion 问答题信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int addObjectiveQuestion(ObjectiveQuestion objectiveQuestion);

    /**
     * 根据考试ID获取客观题数量
     * @param examId 考试ID
     * @return 客观题数量
     * @author 郭欣光
     */
    int getCountByExamId(String examId);

    /**
     * 根据考试ID获取客观题信息
     * @param examId 考试ID
     * @return 客观题信息
     * @author 郭欣光
     */
    List<ObjectiveQuestion> getObjectiveQuestionByExamId(String examId);

    /**
     * 根据客观题ID获取客观题信息
     * @param id 客观题ID
     * @return 客观题信息
     * @author 郭欣光
     */
    ObjectiveQuestion getObjectiveQuestionById(String id);

    /**
     * 删除客观题
     * @param objectiveQuestion 客观题相关信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    int deleteObjectiveQuestion(ObjectiveQuestion objectiveQuestion);
}

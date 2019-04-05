package com.gxg.dao;

import com.gxg.entities.ObjectiveQuestion;

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
}

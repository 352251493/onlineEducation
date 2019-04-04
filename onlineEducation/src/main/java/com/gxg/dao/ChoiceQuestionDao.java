package com.gxg.dao;

import com.gxg.entities.ChoiceQuestion;

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
}

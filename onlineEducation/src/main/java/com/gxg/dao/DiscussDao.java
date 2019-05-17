package com.gxg.dao;

import com.gxg.entities.Discuss;

import java.util.List;

/**
 * 讨论帖子数据库相关
 * @author 郭欣光
 * @date 2019/5/17 19:22
 */
public interface DiscussDao {

    /**
     * 获取讨论帖子个数
     * @return 讨论帖子个数
     */
    int getCount();

    /**
     * 按照创建时间排序获取指定范围的讨论帖子列表
     * @param limitStart 第一个limit
     * @param limitEnd 第二个limit
     * @return 讨论帖子列表
     * @author 郭欣光
     */
    List<Discuss> getDiscussByLimitOrderByCreateTime(int limitStart, int limitEnd);
}

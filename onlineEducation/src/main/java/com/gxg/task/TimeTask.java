package com.gxg.task;

/**
 * 定时任务
 * @author 郭欣光
 * @date 2019/1/17 10:02
 */
public interface TimeTask {

    /**
     * 定时清除未验证用户
     * @author 郭欣光
     */
    void clearUnauthenticatedUsers();

}

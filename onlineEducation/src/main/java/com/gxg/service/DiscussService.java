package com.gxg.service;

import org.json.JSONObject;

/**
 * 讨论帖子相关业务处理接口
 * @author 郭欣光
 * @date 2019/5/17 19:14
 */
public interface DiscussService {

    /**
     * 获取讨论帖子列表
     * @param page 页数
     * @return 讨论帖子相关信息
     * @autjor 郭欣光
     */
    JSONObject getDiscussList(String page);
}

package com.gxg.service.impl;

import com.gxg.dao.DiscussDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Discuss;
import com.gxg.entities.Lesson;
import com.gxg.entities.User;
import com.gxg.service.DiscussService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 讨论帖子相关业务处理
 * @author 郭欣光
 * @date 2019/5/17 19:15
 */
@Service(value = "discussService")
public class DiscussServiceImpl implements DiscussService {

    @Autowired
    private DiscussDao discussDao;

    @Value("${discuss.count.each.page}")
    private int discussCountEachPage;

    @Autowired
    private UserDao userDao;

    /**
     * 获取讨论帖子列表
     *
     * @param page 页数
     * @return 讨论帖子相关信息
     * @autjor 郭欣光
     */
    @Override
    public JSONObject getDiscussList(String page) {
        JSONObject result = new JSONObject();
        String status = "false";
        int discussPageNumber = 1;
        int discussPageInt = 0;
        String hasDiscuss = "false";
        try {
            discussPageInt = Integer.parseInt(page);
        } catch (Exception e) {
            discussPageInt = 0;
        }
        List<Discuss> discussList = null;
        if (discussPageInt > 0) {
            int discussCount = discussDao.getCount();
            if (discussCount != 0) {
                discussPageNumber = ((discussCount % discussCountEachPage) == 0) ? (discussCount / discussCountEachPage) : (discussCount / discussCountEachPage + 1);
                if (discussPageInt <= discussPageNumber) {
                    status = "true";
                    hasDiscuss = "true";
                    discussList = discussDao.getDiscussByLimitOrderByCreateTime(discussPageInt - 1, discussCountEachPage);
                    if (discussList != null && discussList.size() > 0) {
                        for (Discuss discuss : discussList) {
                            if (userDao.getUserCountByEmail(discuss.getUserEmail()) != 0) {
                                User user = userDao.getUserByEmail(discuss.getUserEmail());
                                discuss.setUserName(user.getName());
                            }
                        }
                    }
                }
            } else if (discussPageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("discussPageNumber", discussPageNumber);
        result.accumulate("discussPage", discussPageInt);
        result.accumulate("discussList", discussList);
        result.accumulate("hasDiscuss", hasDiscuss);
        return result;
    }
}

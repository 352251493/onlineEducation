package com.gxg.service.impl;

import com.gxg.dao.LessonDao;
import com.gxg.entities.Lesson;
import com.gxg.service.LessonService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课时相关业务处理
 * @author 郭欣光
 * @date 2019/2/19 10:28
 */

@Service(value = "lessonService")
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDao lessonDao;

    @Value("${lesson.count.each.page}")
    private int lessonCountEachPage;

    /**
     * 获取指定课程指定页数的课时信息
     *
     * @param courseId   课程ID
     * @param lessonPage 页数
     * @return 课时相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getLessonListByCourseId(String courseId, String lessonPage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int lessonPageNumber = 1;
        int lessonPageInt = 0;
        String hasLesson = "false";
        try {
            lessonPageInt = Integer.parseInt(lessonPage);
        } catch (Exception e) {
            lessonPageInt = 0;
        }
        List<Lesson> lessonList = null;
        if (lessonPageInt > 0) {
            int lessonCount = lessonDao.getCountByCourseId(courseId);
            if (lessonCount != 0) {
                lessonPageNumber = ((lessonCount % lessonCountEachPage) == 0) ? (lessonCount / lessonCountEachPage) : (lessonCount / lessonCountEachPage + 1);
                if (lessonPageInt <= lessonPageNumber) {
                    status = "true";
                    hasLesson = "true";
                    lessonList = lessonDao.getLessonByCourseIdAndLimitOrderByModifyTime(courseId, lessonPageInt - 1, lessonCountEachPage);
                }
            } else if (lessonPageInt == 1) {
                status = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("lessonPageNumber", lessonPageNumber);
        result.accumulate("lessonPage", lessonPageInt);
        result.accumulate("lessonList", lessonList);
        result.accumulate("hasLesson", hasLesson);
        return result;
    }
}

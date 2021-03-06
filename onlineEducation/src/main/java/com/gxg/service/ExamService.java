package com.gxg.service;

import com.gxg.entities.Exam;
import com.gxg.entities.Lesson;
import com.gxg.entities.User;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertFalse;
import java.util.List;

/**
 * 考试相关业务处理接口
 * @author 郭欣光
 * @date 2019/3/21 10:49
 */

public interface ExamService {

    /**
     * 创建考试
     * @param courseId 课程ID
     * @param examName 考试名称
     * @param examRequirement 考试要求
     * @param examStartTime 考试最早考试时间
     * @param examEndTime 考试最晚结束时间
     * @param examDuration 考试时长
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    String createExam(String courseId, String examName, String examRequirement, String examStartTime, String examEndTime, String examDuration, HttpServletRequest request);

    /**
     * 获取指定课程指定页数的考试信息
     * @param courseId 课程ID
     * @param examPage 页数
     * @return 考试先关信息
     * @author 郭欣光
     */
    JSONObject getExamListByCourseId(String courseId, String examPage);

    /**
     * 根据考试ID获取考试信息
     * @param examId 考试ID
     * @return 考试信息
     * @author 郭欣光
     */
    Exam getExamById(String examId);

    /**
     * 获取指定课程ID的前N个考试信息
     * @param courseId 课程ID
     * @param topNumber N
     * @return 考试信息
     * @author 郭欣光
     */
    List<Exam> getExamListByCourseIdAndTopNumber(String courseId, int topNumber);

    /**
     * 删除考试信息
     * @param examId 考试ID
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String deleteExam(String examId, HttpServletRequest request);

    /**
     * 获取该用户发布的考试相关信息
     * @param user 用户信息
     * @param examAnalysisPage 页数
     * @return 考试相关信息
     * @author 郭欣光
     */
    JSONObject getMyExamList(User user, String examAnalysisPage);
}

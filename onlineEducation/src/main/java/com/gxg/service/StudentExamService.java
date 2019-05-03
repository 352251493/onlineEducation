package com.gxg.service;

import com.gxg.entities.StudentExam;
import com.gxg.entities.User;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * 学生考试业务相关业务处理接口
 * @author 郭欣光
 * @date 2019/4/13 14:45
 */
public interface StudentExamService {

    /**
     * 获得学生考试相关信息（如果没有则添加）
     * @param examId 考试ID
     * @param userEmail 用户邮箱
     * @return 学生考试信息（如果添加失败则为null）
     * @author 郭欣光
     */
    StudentExam getSetStudentExam(String examId, String userEmail);

    /**
     * 更新考试剩余时间
     * @param examId 考试ID
     * @param examTime 考试剩余时间
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String setStudentExamTime(String examId, String examTime, HttpServletRequest request);

    /**
     * 获取指定考试ID指定页数的学生考试信息
     * @param examId 考试ID
     * @param page 页数
     * @return 学生考试信息
     * @author 郭欣光
     */
    JSONObject getStudentExamListByExamId(String examId, String page);

    /**
     * 根据学生考试ID获取学生考试信息
     * @param studentExamId 学生考试ID
     * @return 学生考试信息
     */
    StudentExam getStudentExamById(String studentExamId);

    /**
     * 计算学生考试成绩
     * @param studentExamId 学生考试ID
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    String setStudentExamScore(String studentExamId, HttpServletRequest request);

    /**
     * 根据用户邮箱获取学生考试数量
     * @param userEmail 用户邮箱
     * @return 学生考试个数
     * @author 郭欣光
     */
    int getStudentExamCountByUserEmail(String userEmail);

    /**
     * 根据用户邮箱获取平均成绩
     * @param userEmail 用户邮箱
     * @return 平均成绩
     * @author 郭欣光
     */
    String getAverageScoreByUserEmail(String userEmail);

    /**
     * 获取指定用户邮箱且大于等于指定成绩的学生信息个数比例
     * @param userEmail 用户邮箱
     * @param score 成绩
     * @return 学生信息个数比例
     * @author 郭欣光
     */
    String getStudentExamCountProportionByUserEmailAndGreaterAndEqualsScore(String userEmail, int score);

    /**
    * 获取指定用户邮箱且大于等于指定成绩的学生考试信息个数
    * @param userEmail 用户邮箱
    * @param score 成绩
    * @return 学生考试信息个数
    * @author 郭欣光
    */
    int getStudentExamCountByUserEmailAndGreaterAndEqualsScore(String userEmail, int score);

    /**
     * 获取不及格率
     * @return 不及格率
     */
    String getFailStudentExamCount(String userEmail);

    /**
     * 根据用户获取学生考试信息
     * @param user 用户
     * @param scorePage 页数
     * @return 学生考试信息
     * @author 郭欣光
     */
    JSONObject getMyStudentExamList(User user, String scorePage);
}

package com.gxg.task.impl;

import com.gxg.dao.UserDao;
import com.gxg.entities.User;
import com.gxg.service.UserService;
import com.gxg.task.TimeTask;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * 定时任务
 * @author 郭欣光
 * @date 2019/1/17 10:04
 */
@Component
public class TimeTaskImpl implements TimeTask {

    @Value("${user.verification.timeout}")
    private long userVerificationTimeout;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    /**
     * 定时清除未验证用户
     * @author 郭欣光
     */
    @Override
    @Scheduled(fixedRate = 900000)
    public void clearUnauthenticatedUsers() {
        System.out.println("INFO:定时任务：定时清除未验证用户开始执行...");
        try {
            if (userDao.getCountByIsVerification("0") == 0) {
                System.out.println("INFO:定时任务：定时清除未验证用户：未发现未完成用户！");
            } else {
                List<User> userList = userDao.getUserByIsVerification("0");
                for (User user : userList) {
                    System.out.println("INFO:定时任务：定时清除未验证用户：检测到用户" + user.getEmail() + "未完成验证");
                    if (user.getCreateTime() == null) {
                        System.out.println("INFO:定时任务：定时清除未验证用户：检测到用户" + user.getEmail() + "创建时间为空");
                        JSONObject deleteUserResult = userService.deleteUser(user);
                        System.out.println("INFO:定时任务：定时清除未验证用户：删除用户" + user.getEmail() + "结果：" + deleteUserResult.toString());
                    } else {
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        long timeDifference = time.getTime() - user.getCreateTime().getTime();
                        System.out.println("INFO:定时任务：定时清除未验证用户：检测到用户" + user.getEmail() + "创建时间为:" + user.getCreateTime().toString() + "，系统时间为：" + time.toString() + "，时间差为：" + timeDifference + "ms，系统要求验证时间差为：" + userVerificationTimeout + "ms");
                        if (timeDifference >= userVerificationTimeout) {
                            System.out.println("INFO:定时任务：定时清除未验证用户：检测到用户" + user.getEmail() + "超时未注册");
                            JSONObject deleteUserResult = userService.deleteUser(user);
                            System.out.println("INFO:定时任务：定时清除未验证用户：删除用户" + user.getEmail() + "结果：" + deleteUserResult.toString());
                        } else {
                            System.out.println("INFO:定时任务：定时清除未验证用户：检测到用户" + user.getEmail() + "未超时");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR:定时任务：定时清除未验证用户出错，错误原因：" + e);
        }
        System.out.println("INFO:定时任务：定时清除未验证用户执行完毕！");
    }
}

package com.gxg.service.impl;

import com.gxg.dao.DiscussDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.*;
import com.gxg.service.DiscussService;
import com.gxg.service.MessageService;
import com.gxg.utils.FileUtils;
import com.gxg.utils.Md5;
import com.gxg.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

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

    @Value("${discuss.resource.dir}")
    private String discussResourceDir;

    @Autowired
    private MessageService messageService;

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

    /**
     * 创建讨论帖子
     *
     * @param discussName    帖子名称
     * @param discussContent 帖子内容
     * @param request        用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String createDiscuss(String discussName, String discussContent, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "创建失败";
        HttpSession session = request.getSession();
        if (StringUtils.isEmpty(discussName)) {
            content = "帖子标题不能为空！";
        } else if (discussName.length() > 100) {
            content = "帖子标题不能超过100字符~";
        } else if (StringUtils.isEmpty(discussContent)) {
            content = "写点内容呗~";
        } else if(session.getAttribute("user") == null) {
            content = "系统未检测到登录用户信息，请刷新页面后重试！";
        } else {
            User user = (User)session.getAttribute("user");
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String timeString = time.toString();
            String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
            while (discussDao.getCountById(id) != 0) {
                long idLong = Long.parseLong(id);
                Random random = new Random();
                idLong += random.nextInt(100);
                id = idLong + "";
                if (id.length() > 17) {
                    id = id.substring(0, 17);
                }
            }
            String filePath = discussResourceDir + Md5.md5(id) + "/";
            JSONObject writeFileResult = FileUtils.writeFile(filePath, Md5.md5(id) + ".html", discussContent);
            if ("true".equals(writeFileResult.getString("status"))) {
                boolean insertSuccess = false;
                Discuss discuss = new Discuss();
                discuss.setId(id);
                discuss.setName(discussName);
                discuss.setContent(Md5.md5(id) + "/" + Md5.md5(id) + ".html");
                discuss.setCreateTime(time);
                discuss.setUserEmail(user.getEmail());
                try {
                    if (discussDao.createDiscuss(discuss) == 0) {
                        System.out.println("ERROR:创建讨论帖子时操作数据库失败");
                        content = "操作数据库失败！";
                    } else {
                        status = "true";
                        insertSuccess = true;
                        content = id;
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:创建讨论帖子时操作数据库失败，失败原因：" + e);
                    content = "操作数据库失败！";
                }
                if (insertSuccess) {
                    String messageTitle = "您已成功发表讨论帖子";
                    String messageContent = createCreateDiscussSuccessEmailMessage(discuss);
                    JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                    System.out.println("INFO:讨论帖子" + discuss.toString() + "创建成功时创建消息通知结果：" + createMessageResult.toString());
                } else {
                    JSONObject deleteFileResult = FileUtils.deleteFile(filePath + Md5.md5(id) + ".html");
                    System.out.println("INFO:创建讨论帖子" + discuss.toString() + "操作数据库失败后删除文件结果:" + deleteFileResult);
                }
            } else {
                content = "系统写入文件失败，失败原因：" + writeFileResult.getString("content");
                System.out.println("ERROR:系统创建讨论帖子时写入文件失败，失败原因：" + writeFileResult.getString("content"));
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createCreateDiscussSuccessEmailMessage(Discuss discuss) {
        String timeString = discuss.getCreateTime().toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "发表讨论帖子：" + discuss.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }

    /**
     * 根据ID获取讨论帖子信息
     *
     * @param discussId 讨论帖子ID
     * @return 讨论帖子信息
     * @author 郭欣光
     */
    @Override
    public Discuss getDiscussById(String discussId) {
        if (discussDao.getCountById(discussId) == 0) {
            return null;
        } else {
            Discuss discuss = discussDao.getDiscussById(discussId);
            if (userDao.getUserCountByEmail(discuss.getUserEmail()) != 0) {
                User user = userDao.getUserByEmail(discuss.getUserEmail());
                discuss.setUserName(user.getName());
            }
            String discussFile = discussResourceDir + discuss.getContent();
            JSONObject fileContent = FileUtils.readFile(discussFile);
            discuss.setContent(fileContent.getString("content"));
            return discuss;
        }
    }

    /**
     * 根据用户邮箱获取指定页数的讨论帖子信息
     *
     * @param userEmail   用户邮箱
     * @param discussPage 讨论帖子页数
     * @return 讨论帖子相关信息
     * @author 郭欣光
     */
    @Override
    public JSONObject getDiscussListByUserEmail(String userEmail, String discussPage) {
        JSONObject result = new JSONObject();
        String status = "false";
        int discussPageNumber = 1;
        int discussPageInt = 0;
        String hasDiscuss = "false";
        try {
            discussPageInt = Integer.parseInt(discussPage);
        } catch (Exception e) {
            discussPageInt = 0;
        }
        List<Discuss> discussList = null;
        if (discussPageInt > 0) {
            int discussCount = discussDao.getCountByUserEmail(userEmail);
            if (discussCount != 0) {
                discussPageNumber = ((discussCount % discussCountEachPage) == 0) ? (discussCount / discussCountEachPage) : (discussCount / discussCountEachPage + 1);
                if (discussPageInt <= discussPageNumber) {
                    status = "true";
                    hasDiscuss = "true";
                    discussList = discussDao.getDiscussByUserEmailAndLimitOrderByCreateTime(userEmail, discussPageInt - 1, discussCountEachPage);
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

    /**
     * 删除讨论帖子
     *
     * @param discussId 讨论帖子
     * @param request 用户请求相关信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteDiscuss(String discussId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "系统未检测到登录用户信息，请刷新页面后重试！";
        } else if (discussDao.getCountById(discussId) == 0) {
            content = "系统未检测到该讨论帖子信息！";
        } else {
            User user = (User)session.getAttribute("user");
            Discuss discuss = discussDao.getDiscussById(discussId);
            if (user.getEmail().equals(discuss.getUserEmail())) {
                try {
                    if (discussDao.deleteDiscuss(discuss) == 0) {
                        System.out.println("ERROR:删除讨论帖子信息" + discuss.toString() + "操作数据库失败");
                        content = "操作数据库失败！";
                    } else {
                        status = "true";
                        content = "删除成功！";
                    }
                } catch (Exception e) {
                    System.out.println("ERROR:删除讨论帖子信息" + discuss.toString() + "操作数据库失败，失败原因：" + e);
                    content = "操作数据库失败！";
                }
                if ("true".equals(status)) {
                    JSONObject deleteFileResult = FileUtils.deleteFile(discussResourceDir + discuss.getContent());
                    System.out.println("INFO:删除讨论帖子" + discuss.toString() + "成功后，删除文件结果：" + deleteFileResult.toString());
                    String messageTitle = "删除讨论帖子成功";
                    String messageContent = createDeleteDiscussSuccessEmailMessage(discuss);
                    JSONObject createMessageResult = messageService.createMessage(user.getEmail(), messageTitle, messageContent);
                    System.out.println("INFO:讨论帖子" + discuss.toString() + "删除成功时创建消息通知结果：" + createMessageResult.toString());
                }
            } else {
                content = "您没有删除他人讨论帖子的权限！";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private String createDeleteDiscussSuccessEmailMessage(Discuss discuss) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String timeString = time.toString().split("\\.")[0];
        String message = "<p>恭喜您，您于" + timeString + "删除讨论帖子：" + discuss.getName() + "&nbsp;&nbsp;成功！</p>";
        message += "<p style='color: red;'>如果不是您本人操作，可能密码已经泄露，请尽快修改密码！</p>";
        return message;
    }
}

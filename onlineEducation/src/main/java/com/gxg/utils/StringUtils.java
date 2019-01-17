package com.gxg.utils;

import java.util.regex.Pattern;

/**
 * 字符串先关工具类
 * @author 郭欣光
 * @date 2019/1/16 14:49
 */
public class StringUtils {

    //邮箱正则表达式
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 判断字符串是否为空
     * @param string 要判断的字符创
     * @return true 或 false
     * @author 郭欣光
     */
    public static boolean isEmpty(String string) {
        if (string == null || "".equals(string.trim()) || string.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验邮箱
     *
     * @param email 电子邮箱地址
     * @return 校验通过返回true，否则返回false
     * @author 郭欣光
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }
}

package com.maxqiu.blog.common;

import java.util.List;
import java.util.Random;

/**
 * 用户服务常量
 *
 * @author Max_Qiu
 */
public class UserConstant {
    /**
     * 昵称合集
     */
    public static List<String> nicknameList;

    /**
     * 获取随机昵称
     */
    public static String getRandomNickname() {
        return UserConstant.nicknameList.get(new Random().nextInt(108));
    }
}

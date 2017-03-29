package com.imis.jxufe.user.facade;

import com.imis.jxufe.base.model.IschoolUser;

import java.util.Map;

/**
 * @author zhongping
 * @date 2017/3/21
 */
public interface UserServiceFacade {


    /**
     * 检查用户的邮箱是否存在
     * @param email
     * @return
     */
    boolean userEmailIsExist(String email);

    /**
     * 插入数据库
     * @param user
     * @return
     */
    Map<String ,Object> createUser(IschoolUser user);

    /**
     * 预创建用户
     * @param email
     * @param name
     * @param passwd
     * @return
     */
     String preCreateUser(String email, String name, String passwd,Integer type);

    /**
     * 用户登录接口
     * @param userName
     * @param passwd
     * @param type
     * @return
     */
     String login(String userName, String passwd, Integer type);
}

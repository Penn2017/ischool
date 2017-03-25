package com.imis.jxufe.user.facade;

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
     * 创建一个用户
     * @param email
     * @param name
     * @param passwd
     * @return
     */
    boolean createUser(String email,String name,String passwd);






}

package com.imis.jxufe.user.facade;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhongping
 * @date 2017/3/21
 */
public interface UserService {


    /**
     * 根据用户的userKey判断用户的key是否存在
     * @param userKey
     * @return
     */
    @Transactional(readOnly = true)
    boolean userIsExist(String userKey);


}

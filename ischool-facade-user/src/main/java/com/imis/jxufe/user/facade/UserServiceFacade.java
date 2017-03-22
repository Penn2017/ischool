package com.imis.jxufe.user.facade;

import com.imis.jxufe.model.IschoolUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

/**
 * @author zhongping
 * @date 2017/3/21
 */
public interface UserServiceFacade {


    /**
     * 根据用户的userKey判断用户的key是否存在
     * @param userKey
     * @return
     */
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    boolean userIsExist(String userKey);


    /**
     * 创建用户
     * @param unStoreUser
     * @return
     */
    @Transactional
    IschoolUser  createUser(@Valid IschoolUser unStoreUser, BindingResult errorResult);



}

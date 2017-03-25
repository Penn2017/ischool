package com.imis.jxufe.user.facade.service.impl;

import com.imis.jxufe.user.facade.UserServiceFacade;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 对外暴露的用户服务接口
 *
 * @author zhongping
 * @date 2017/3/21
 */
@Component("userService")
@Transactional
public class UserServiceFacadeImpl implements UserServiceFacade {


    @Override
    @Transactional(readOnly = true)
    public boolean userEmailIsExist(String email) {
        return false;
    }


    @Override
    public boolean createUser(String email, String name, String passwd) {
        return false;
    }

}

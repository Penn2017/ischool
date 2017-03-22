package com.imis.jxufe.user.facade.service.impl;

import com.imis.jxufe.core.service.RedisService;
import com.imis.jxufe.model.IschoolUser;
import com.imis.jxufe.user.facade.UserServiceFacade;
import com.imis.jxufe.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

/**
 * 对外暴露的用户服务接口
 *
 * @author zhongping
 * @date 2017/3/21
 */
@Component("userService")
public class UserServiceFacadeImpl implements UserServiceFacade {

    @Autowired
    private UserMapper userDao;

    @Autowired
    private RedisService redis;

    @Override
    public boolean userIsExist(String userKey) {
        IschoolUser user = new IschoolUser();

        return false;
    }


    @Override
    public IschoolUser createUser(@Valid IschoolUser unStoreUser,
                                  BindingResult errorResult) {


        System.out.println(errorResult);

        return null;
    }
}

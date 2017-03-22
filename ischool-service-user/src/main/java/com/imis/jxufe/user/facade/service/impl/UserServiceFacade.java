package com.imis.jxufe.user.facade.service.impl;

import com.imis.jxufe.core.service.RedisService;
import com.imis.jxufe.user.facade.UserService;
import com.imis.jxufe.user.mapper.UserMapper;
import com.imis.jxufe.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 对外暴露的用户服务接口
 * @author zhongping
 * @date 2017/3/21
 */
@Component("userService")
public class UserServiceFacade implements UserService {

    @Autowired
    private UserMapper userDao;

    @Autowired
    private RedisService redis;

    @Override
    public boolean userIsExist(String userKey) {
        User user=new User();
        user.setId(Integer.valueOf(userKey));

        return userDao.selectOne(user)==null;
    }
}

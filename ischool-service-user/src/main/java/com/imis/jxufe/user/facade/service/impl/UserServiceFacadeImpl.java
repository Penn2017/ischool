package com.imis.jxufe.user.facade.service.impl;

import com.imis.jxufe.model.Constant;
import com.imis.jxufe.model.IschoolUser;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import com.imis.jxufe.user.facade.UserServiceFacade;
import com.imis.jxufe.user.mapper.UserMapper;
import com.imis.jxufe.utils.IdWorker;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户服务接口实现
 *
 * @author zhongping
 * @date 2017/3/21
 */
@Component("userService")
public class UserServiceFacadeImpl implements UserServiceFacade {


    @Resource(name = "redisServiceFacade")
    private RedisServiceFacade redisServiceFacade;
    /**
     * reids服务
     */

   @Autowired
    private UserMapper userMapper;

    /**
     * id生成器
     */
    private IdWorker idWorker = new IdWorker(1, 0);


    @Override
    @Transactional(readOnly = true)
    public boolean userEmailIsExist(String email) {
        //redis里面寻找
        Set emailSet = redisServiceFacade.getObject(Constant.ALL_MAIL_SET, HashSet.class);
        if (emailSet != null && !emailSet.isEmpty()) {
            if (emailSet.contains(email)) {
                return true;
            }
        }
        //数据库中寻找
        int count = userMapper.selectCount(new IschoolUser(email));
        return count > 0;
    }

    public String preCreateUser(String email, String name, String passwd,Integer type) {
        Map<String, Object> resutl = new HashMap();

        //验证邮箱
        boolean userEmailIsExist = userEmailIsExist(email);
        if (userEmailIsExist) {
            return Constant.USER_IS_EXIST;
        }
        //密码加密
        String encodingPasswd = new String(DigestUtils.md5(passwd));
        Date now = new Date();
        //创建user
        IschoolUser ischoolUser = new IschoolUser(email, name, encodingPasswd, now, now);
        //设置用户类型
        ischoolUser.setType(type);
        //设置账户未激活
        ischoolUser.setState(0);
        //生成激活key
        String enableKey = String.valueOf(idWorker.nextId());

        //存入redis，等待激活,保留两天的时间
        redisServiceFacade.setObjectExpire(enableKey, ischoolUser, Constant.USER_KEY_LIVE_TIME.intValue());

        return enableKey;
    }

    @Override
    @Transactional
    public Map<String, Object> createUser(IschoolUser user) {
        Map<String, Object> resutl = new HashMap();
        //记录到数据库
        int success = userMapper.insert(user);
        return resutl;
    }

}

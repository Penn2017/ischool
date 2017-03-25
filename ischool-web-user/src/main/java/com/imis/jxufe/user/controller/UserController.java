package com.imis.jxufe.user.controller;

import com.imis.jxufe.facade.SenderMailServiceFacade;
import com.imis.jxufe.model.Constant;
import com.imis.jxufe.param.MailParam;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import com.imis.jxufe.user.facade.UserServiceFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

/**
 * @author zhongping
 * @date 2017/3/25
 */
@RestController
public class UserController {

    @Resource(name = "senderMailServiceFacade")
    private SenderMailServiceFacade senderMailService;

    @Resource(name = "redisServiceFacade")
    private RedisServiceFacade redisService;


    @Resource(name = "userServiceFacade")
    private UserServiceFacade userService;

    @RequestMapping("/preRegist")
    public Map<String,Object> preRegist(String email, String name, String passwd){
        Map<String,Object> result=new HashMap();
        String enableKey = userService.preCreateUser(email, name, passwd);

        if (StringUtils.equals(enableKey, Constant.USER_IS_EXIST)) {
            //用户已经存在
            result.put("ret_msg", "用户已经存在！");
        }else {
            //预注册成功发送邮件
            if (StringUtils.isNotEmpty(enableKey)) {
                MailParam mail = new MailParam();
                mail.setTo(email);
                mail.setSubject("ischool账号激活通知#"+new Random().nextInt());
                mail.setContent(StringUtils.replace(
                        Constant.MAIL_CONTENT,
                        Constant.URL_PLACEHOLDER,
                        "http://baidu.com?enableKey="+enableKey));

                //发送邮件
                senderMailService.send(mail);

                HashSet mailCustomers = redisService.getObject(Constant.ALL_MAIL_SET, HashSet.class);
                if (mailCustomers==null) {
                    mailCustomers = new HashSet();
                }
                mailCustomers.add(email);
                redisService.setObject(Constant.ALL_MAIL_SET, mailCustomers);

            }
            result.put("ret_msg", "注册成功，请到邮箱去激活！");
        }

        return result;
    }





}



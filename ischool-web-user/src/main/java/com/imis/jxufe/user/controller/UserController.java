package com.imis.jxufe.user.controller;

import com.imis.jxufe.facade.SenderMailServiceFacade;
import com.imis.jxufe.model.Constant;
import com.imis.jxufe.model.IschoolUser;
import com.imis.jxufe.model.ResponseEntity;
import com.imis.jxufe.param.MailParam;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import com.imis.jxufe.user.facade.UserServiceFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

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


    //声明函数
    Function<String, String> trimOrDefault
            = (String str) -> StringUtils.defaultIfBlank(StringUtils.trim(str), "");


    @RequestMapping("/preRegist")
    public ResponseEntity preRegist(@RequestParam(name = "email")String email,
                                    @RequestParam(name = "name") String name,
                                    @RequestParam(name = "passwd")String passwd,Integer type){
        ResponseEntity result=null;
        //进行预注册
        String enableKey = userService.preCreateUser(trimOrDefault.apply(email), trimOrDefault.apply(name), trimOrDefault.apply(passwd),type);


        if (StringUtils.equals(enableKey, Constant.USER_IS_EXIST)) {
            //用户已经存在
            result = new ResponseEntity(440,"用户已经存在！");
        }else {
            //预注册成功发送邮件
            if (StringUtils.isNotEmpty(enableKey)) {
                MailParam mail = new MailParam();
                mail.setTo(email);
                mail.setSubject("ischool账号激活通知#"+ LocalDateTime.now().getNano());
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
            result = new ResponseEntity(200, "注册成功，请到邮箱去激活！");
        }

        return result;
    }

    /**
     * 激活用户
     * @return
     */
    @RequestMapping("/enableUser")
    public ResponseEntity  enableUser(@RequestParam(name = "enableKey")String enableKey){
        ResponseEntity result=null;
        IschoolUser user = redisService.getObject(enableKey, IschoolUser.class);
        if (user==null) {
            result = new ResponseEntity(404,"用户已经是激活状态");
        }else{
            //可用状态
            user.setState(1);
            user.setModifyTime(new Date());
            //创建用户
            userService.createUser(user);
            result = new ResponseEntity(200,"用户已经激活");
        }
        return result;
    }


    /**
     *用户登录
     */
    @RequestMapping("/login")
    public ResponseEntity login(@RequestParam(name = "email")String email ,
                                @RequestParam(name = "passwd")String  passwd){
        ResponseEntity result =null;

        String userToken=userService.login(email,passwd);

        if (StringUtils.equals(userToken, Constant.USERNAME_OR_PASSWD_ERRO)) {
            //用户名或者密码错误
            result = new ResponseEntity(404, "用户名或者密码错误");
        }else {
            result = new ResponseEntity(200, "登录成功");

            IschoolUser user=redisService.getObject(userToken, IschoolUser.class);
            Map<String, Object> params = result.getParams();
            params.put("userToken", userToken);
            user.setPasswd(null);
            params.put("user", user);
        }

        return result;
    }

    /**
     * 判断用户是否已经登录
     * @return
     */
    @RequestMapping("/userIsLogin")
    public ResponseEntity userIsLogin(@RequestParam(name = "userToken")
                                                  String userToken){
        ResponseEntity result=null;
        String token=trimOrDefault.apply(userToken);
        String userJson = redisService.get(token);
        if (StringUtils.isEmpty(userJson)) {
            result = new ResponseEntity(404, "没有登录");
        }else{
            result = new ResponseEntity(200, "已经登录");
        }
        return result;
    }

    /**
     * 获取用户的相关信息
     * @param userToken
     * @return
     */
    @RequestMapping("/getUserLoginInfo")
    public ResponseEntity getUserLoginInfo(@RequestParam(name = "userToken")
                                                       String userToken){
        ResponseEntity result = new ResponseEntity();
        IschoolUser user=redisService.getObject(userToken, IschoolUser.class);
        result.getParams() .put("userToken", userToken);
        return result;
    }



}



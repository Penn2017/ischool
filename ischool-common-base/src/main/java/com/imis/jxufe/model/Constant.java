package com.imis.jxufe.model;

import java.io.Serializable;

/**
 * @author zhongping
 * @date 2017/3/25
 */
public class Constant  implements Serializable {


    private Constant() {
        /***
         * empty private constant.
         */
    }

    //所有已经注册人的邮箱
    public static final String ALL_MAIL_SET ="all_mail_set" ;
    public static final String USERNAME_OR_PASSWD_ERRO = "username_or_passwd_erro";
    public static String USER_IS_EXIST="user_is_exist";
    public static Integer USER_LOGIN_VALIDTE_TIME = 60 * 30;


    private static String LOG_URL = "http://oli0puuwc.bkt.clouddn.com/logo.png";


    public static String URL_PLACEHOLDER = "urlpalceholder9988";
    public static String MAIL_CONTENT=
            "<html>欢迎您注册ischool，请在2天内点击下面链接激活账号。<br/><a href=\""
                    + URL_PLACEHOLDER
                    + "\">"+URL_PLACEHOLDER+"</a><br/><br/><br/>"
                    +"<img src=\""+LOG_URL+"\"/><br/>"
                    +"【<b>Join class anytime ,anywhere | ISchool.</b>】</html>";

    /**
     * 用户预注册时候key存活时间
     */
    public static Long USER_KEY_LIVE_TIME=60 * 60 * 24 * 2L;

    public static String USER_TOKEN="userToken";
}

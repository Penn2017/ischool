package com.imis.jxufe.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zhongping
 * @date 2017/3/27
 */
public class MD5Utils {

    private static Logger logger = LoggerFactory.getLogger(MD5Utils.class);
    /**
     * 密码加密
     * @param passwd
     * @return
     */
    public  static  String encodMD5(String passwd) {
        String encodingPasswd=null;
        //确定计算方法
        MessageDigest md5= null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.debug(e.getMessage());
        }
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        try {
            encodingPasswd=base64en.encode(md5.digest(passwd.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            logger.debug(e.getMessage());
        }
        return encodingPasswd;
    }
}

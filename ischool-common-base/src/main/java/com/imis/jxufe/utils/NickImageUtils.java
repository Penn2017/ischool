package com.imis.jxufe.utils;

import java.util.Random;

/**
 * @author zhongping
 * @date 2017/3/27
 */
public class NickImageUtils {


    private NickImageUtils(){
        /**
         * empty privte
         */
    }

    /**
     * 预设头像集合
     */
    private static String [] urls={"http://oli0puuwc.bkt.clouddn.com/head5.jpg",
            "http://oli0puuwc.bkt.clouddn.com/head1.jpg",
            "http://oli0puuwc.bkt.clouddn.com/head4.jpeg",
            "http://oli0puuwc.bkt.clouddn.com/head3.jpg",
             "http://oli0puuwc.bkt.clouddn.com/head2.jpeg"};


    public static String getRandomNickPic(){
        Random random = new Random(urls.length);
        int index= random.nextInt();
        if (index>=0&&index<urls.length) {
            return urls[index];
        }else {
            return urls[0];
        }

    }


}

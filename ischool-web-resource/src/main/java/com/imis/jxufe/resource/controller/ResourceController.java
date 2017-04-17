package com.imis.jxufe.resource.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

/**
 * 内网资源下载类
 * @author zhongping
 * @date 2017/4/9
 */

@Controller
public class ResourceController {

    // endpoint  :内网域名
    //private static  final String endpoint = "http://oss-cn-shanghai-internal.aliyuncs.com/";
    private static  final String endpoint = "http://oss-cn-shanghai.aliyuncs.com/";
    // accessKey请登录https://ak-console.aliyun.com/#/查看
    private static  final String accessKeyId = "LTAI4TPzfMR4o0ic";
    private static  final String accessKeySecret = "eWjyDSHLQFSa6vA6tSoCWXSoFed0WC";
     private static  final String bucketName = "ischool2017";

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceController.class);



     @RequestMapping("/getfile")
    public void   getfile(@RequestParam("url") String url){

         //http://files.jxufe-ischool.top/images/AXP5FyeNCi.png
        //解析URL
         StringUtils.lastIndexOf(url, "/");


        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 下载object到文件
        ossClient.getObject(new GetObjectRequest(bucketName, "<yourKey>"), new File("test"));
        // 关闭client
        ossClient.shutdown();
    }



   //@Test
    public void test1(){

        //http://files.jxufe-ischool.top/images/AXP5FyeNCi.png

        String url = "http://files.jxufe-ischool.top/videos/bnT2YwKTPA.mp4";
        //解析URL
        String infos[] = StringUtils.split(url, "/");

        int length = infos.length;
        String fileName = infos[length - 1].trim();
        String path = infos[length - 2].trim();

        LOGGER.debug("------------"+fileName);
        LOGGER.debug("------------"+path);


        String resourceName = path+"/"+ fileName ;
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);


        // 下载object到文件
        ossClient.getObject(new GetObjectRequest(bucketName, resourceName), new File("test"));
        // 关闭client
        ossClient.shutdown();

    }










}

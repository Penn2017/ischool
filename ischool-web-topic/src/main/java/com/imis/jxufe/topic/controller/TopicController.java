package com.imis.jxufe.topic.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子的controller
 * @author zhongping
 * @date 2017/4/6
 */
@RestController
public class TopicController {



    /**
     * 新建立一个公开贴
     * @return
     */
    @RequestMapping(value = "createTopic")
    public ResponseEntity createTopic(){


        return null;
    }


    /**
     * 评论一个帖子
     * @return
     */
    @RequestMapping(value = "commentTopic")
    public ResponseEntity  commentTopic(){

        return null;
    }


    /**
     * 查询所有的帖子
     * @return
     */
    @RequestMapping(value = "queryAllOpenTopic")
    public ResponseEntity  queryAllOpenTopic(){

        return null;
    }

    /**
     * 进入一个开放帖子
     * @return
     */
    @RequestMapping(value = "enterOpenTopic")
    public ResponseEntity  enterOpenTopic(){

        return null;
    }









}

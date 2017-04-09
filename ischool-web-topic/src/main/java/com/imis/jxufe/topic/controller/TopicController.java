package com.imis.jxufe.topic.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.topic.CommentNode;
import com.imis.jxufe.base.model.topic.PostTopic;
import com.imis.jxufe.base.model.topic.TopicComment;
import com.imis.jxufe.topic.facade.TopicServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 帖子的controller
 * @author zhongping
 * @date 2017/4/6
 */
@RestController
public class TopicController {



    @Autowired
    private TopicServiceFacade topicService;


    /**
     * 新建立一个公开贴
     * @return
     */
    @RequestMapping(value = "/createTopic")
    public ResponseEntity createTopic(PostTopic topic){

        ResponseEntity result=null;
        Integer topicId=topicService.addTopic(topic);

        if (topicId!=null) {
            result = new ResponseEntity(200, "发帖成功");
            result.getParams().put("id", topicId);
            return result;
        }

        //提示发帖失败
        result = new ResponseEntity(400, "发帖失败");

        return result;
    }


    /**
     * 评论一个帖子
     * @return
     */

    @RequestMapping(value = "/commentTopic")
    public ResponseEntity  commentTopic(TopicComment topicComment){
        ResponseEntity result=null;
        Integer commentId=topicService.commentTopic(topicComment);

        if (commentId!=null) {
            result=new  ResponseEntity(200,"评论成功");
            result.getParams().put("id", result);
            return result;
        }
        return new  ResponseEntity(400,"网络异常，请重试");

    }


    /**
     * 查询所有的帖子
     * @return
     */
    @RequestMapping(value = "/queryAllOpenTopic")
    public ResponseEntity  queryAllOpenTopic(){
      /*****************只查询公开帖子***********************/
       List<PostTopic> topics= topicService.queryAllOpenTopic();
        if (topics==null||topics.size()==0) {
            return new ResponseEntity(404,"很抱歉，当前没有任何热帖，赶快去抢沙发吧！");
        }

        ResponseEntity result = new ResponseEntity(200, "查询成功");
        result.getParams().put("rows", topics);

        return result;
    }



    /**
     * 进入一个开放帖子
     * @return
     */
    @RequestMapping(value = "enterOpenTopic/{topicId}")
    public ResponseEntity  enterOpenTopic(@PathVariable("topicId") Integer topicId){
        ResponseEntity result=null;

         List<CommentNode>  commentNodes= topicService.queryTopicComment(topicId);
        if (commentNodes==null||commentNodes.size()>0) {
            result = new ResponseEntity(404, "该帖子还没有评论，赶紧抢沙发吧");
            return result;
        }

        result = new ResponseEntity(200, "查询成功");
        result.getParams().put("rows", commentNodes);

        return result;
    }









}

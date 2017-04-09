package com.imis.jxufe.topic.facade;

import com.imis.jxufe.base.model.topic.CommentNode;
import com.imis.jxufe.base.model.topic.PostTopic;
import com.imis.jxufe.base.model.topic.TopicComment;

import java.util.List;

/**
 * @author zhongping
 * @date 2017/4/6
 */
public interface TopicServiceFacade {

    /**
     * 发帖
     * @param topic
     * @return
     */
    Integer addTopic(PostTopic topic);

    /**
     * 评论一个帖子
     * @param topicComment
     * @return
     */
    Integer commentTopic(TopicComment topicComment);

    /**
     * 查询所有
     * @return
     */
    List<PostTopic> queryAllOpenTopic();

    /**
     * 查询帖子的显示结构
     * @param topicId
     * @return
     */
    List<CommentNode> queryTopicComment(Integer topicId);
}

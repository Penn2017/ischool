package com.imis.jxufe.topic.facade.impl;

import com.imis.jxufe.base.model.IschoolUser;
import com.imis.jxufe.base.model.topic.CommentNode;
import com.imis.jxufe.base.model.topic.PostTopic;
import com.imis.jxufe.base.model.topic.TopicComment;
import com.imis.jxufe.topic.facade.TopicServiceFacade;
import com.imis.jxufe.topic.mapper.TopicMapper;
import com.imis.jxufe.topic.mapper.commentTopicMapper;
import com.imis.jxufe.user.facade.UserServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhongping
 * @date 2017/4/6
 */
@Service("topicServiceFacadeImpl")
public class TopicServiceFacadeImpl implements TopicServiceFacade {


    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private commentTopicMapper commentTopicMapper;

    @Autowired
    private UserServiceFacade userService;


    @Override
    @Transactional
    public Integer addTopic(PostTopic topic) {
        //设置时间
        Date now = new Date();
        topic.setCreateTime(now);
        topic.setLastJoinTime(now);
        try {
            topicMapper.insert(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return topic.getId();
    }

    @Override
    @Transactional
    public Integer commentTopic(TopicComment topicComment) {
        topicComment.setCommentTime(new Date());

        try {
            commentTopicMapper.insert(topicComment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return topicComment.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostTopic> queryAllOpenTopic() {
        List<PostTopic> topics = topicMapper.selectAll();
        List<PostTopic> collect = topics.stream().filter((e) -> {
            //只是查询所有的公开帖子
            if (e.getCourseId() != null) {
                return false;
            }
            return true;

        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<CommentNode> queryTopicComment(Integer topicId) {

        //找出主评论
        TopicComment topicComment=new TopicComment();
        topicComment.setTopicId(topicId);
        topicComment.setParentId(0);

        List<TopicComment> majorComment = commentTopicMapper.select(topicComment);

        if (majorComment==null||majorComment.size()==0) {
            //没有评论
            return null;
        }
        List<CommentNode>  nodes=majorComment.stream().map((e) -> {
            CommentNode major = new CommentNode(e.getId(), e.getCommentUserId(), e.getCommentUserName(),
                    e.getCommentContent(), e.getCommentTime(), e.getTopicId(), e.getParentId());

            IschoolUser user = userService.selectOneUser(String.valueOf(e.getCommentUserId()));

            //设置头像
            major.setCommentImageUrl(user.getImage());

            //找出其子评论
            TopicComment sonComment = new TopicComment();
            sonComment.setParentId(major.getId());
            List<TopicComment> childrenComment = commentTopicMapper.select(sonComment);
            //收集其子评论
            List<CommentNode> children =
                    childrenComment.stream().map((k) -> new CommentNode(k.getId(), k.getCommentUserId(), k.getCommentUserName(),
                             k.getCommentContent(), k.getCommentTime(), k.getTopicId(), k.getParentId())).collect(Collectors.toList());

            major.setChildren(children);
            return major;
        }).collect(Collectors.toList());

        return nodes;
    }

    @Override
    public PostTopic queryOneTopicById(Integer topicId) {
        return topicMapper.selectByPrimaryKey(topicId);
    }

    @Override
    public List<PostTopic> queryCourseTopic(Integer courseId) {
        List<PostTopic> topics = topicMapper.selectAll();
        List<PostTopic> collect = topics.stream().filter((e) -> {
            //只是查询所有的公开帖子
            if (e.getCourseId() != null&&e.getCourseId().equals(courseId)) {
                return true;
            }
            return false;

        }).collect(Collectors.toList());
        return collect;
    }
}

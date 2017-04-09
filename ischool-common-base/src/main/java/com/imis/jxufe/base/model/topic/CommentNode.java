package com.imis.jxufe.base.model.topic;

import java.util.Date;
import java.util.List;

/**
 * 帖子显示的前台结构
 * @author zhongping
 * @date 2017/4/6
 */
public class CommentNode {

    private Integer id;
    private Integer commentUserId;
    private String commentUserName;

    private String  commentContent;
    private Date  commentTime;

    private Integer topicId;
    private Integer parentId;

    private List<CommentNode> children;


    public CommentNode(Integer id, Integer commentUserId, String commentUserName, String commentUontent, Date commentTime, Integer topicId, Integer parentId) {
        this.id = id;
        this.commentUserId = commentUserId;
        this.commentUserName = commentUserName;
        this.commentContent = commentUontent;
        this.commentTime = commentTime;
        this.topicId = topicId;
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUontent() {
        return commentContent;
    }

    public void setCommentUontent(String commentUontent) {
        this.commentContent = commentUontent;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public List<CommentNode> getChildren() {
        return children;
    }

    public void setChildren(List<CommentNode> children) {
        this.children = children;
    }
}

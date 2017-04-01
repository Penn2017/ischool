package com.imis.jxufe.base.model.course;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "post_topic")
public class PostTopic  implements Serializable {
    @Id
    private Integer id;

    private String title;

    private String content;

    private String imageUrl;

    private Integer createUserId;

    private String createUserName;

    private String createUserImage;

    private Date createTime;

    private Date lastJoinTime;

    private Integer courseId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    public String getCreateUserImage() {
        return createUserImage;
    }

    public void setCreateUserImage(String createUserImage) {
        this.createUserImage = createUserImage == null ? null : createUserImage.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastJoinTime() {
        return lastJoinTime;
    }

    public void setLastJoinTime(Date lastJoinTime) {
        this.lastJoinTime = lastJoinTime;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
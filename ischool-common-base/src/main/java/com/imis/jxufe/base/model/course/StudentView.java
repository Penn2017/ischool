package com.imis.jxufe.base.model.course;

import java.io.Serializable;

/**
 * 学生视图
 * @author zhongping
 * @date 2017/4/1
 */
public class StudentView implements Serializable {

    private Integer id;
    private  String email;
    private String name;

    private String image;

    private Integer state;//学生在这堂课上面的状态

    private Integer courseId;


    public StudentView(Integer id, String email, String name, String image, Integer state) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.image = image;
        this.state = state;
    }

    public StudentView() {
        /**
         * empty
         */
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}

package com.imis.jxufe.user.model;

/**
 * 学生查询我的课程的视图
 * @author zhongping
 * @date 2017/4/1
 */
public class CourseView {

    private Integer courseId;
    private String courseName;

    private String courseNotice;
    private String imageUrl;

    /**我的课程的状态*/
    private String myState;

    public CourseView(Integer courseId, String courseName, String courseNotice, String imageUrl, String myState) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseNotice = courseNotice;
        this.imageUrl = imageUrl;
        this.myState = myState;
    }

    public CourseView() {
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseNotice() {
        return courseNotice;
    }

    public void setCourseNotice(String courseNotice) {
        this.courseNotice = courseNotice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMyState() {
        return myState;
    }

    public void setMyState(String myState) {
        this.myState = myState;
    }
}

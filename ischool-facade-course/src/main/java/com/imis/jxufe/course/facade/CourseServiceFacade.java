package com.imis.jxufe.course.facade;

import com.imis.jxufe.base.model.IschoolUser;
import com.imis.jxufe.base.model.SimpleResponse;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.base.model.course.StudentView;

import java.util.List;
import java.util.Map;

/**
 * 课程接口
 * @author zhongping
 * @date 2017/3/29
 */
public interface CourseServiceFacade {


    /**
     * 添加课程
      * @param course
     * @return
     */
    Integer addCourse(Course course);


    /**
     * 删除课程
     * @param course
     * @return
     */
    SimpleResponse deleteCourse(Course course);


    /***
     * 修改课程
     * @return
     */
    boolean updateCourse(Course course);

    /**
     * 课程详情
     * @return
     */
    Course courseDetail(Integer courseId);


    /**
     * 一个老师所有的课程
     * @param teacherId
     * @return
     */
    List<Course>  allCourses(Integer teacherId);


    /**
     * 根据邀请码查询课程
     * @param joinId
     * @return
     */
    Course getCourseByInviteId(String joinId);

    /**
     * 根据一门课程的id查询一门课程
     * @param courseId
     * @return
     */
    Course selectOneCourseById(String courseId);

    /**
     * 将学生加入课堂
     * @param student
     * @param course
     */
    void applyOneCourse(IschoolUser student, Course course);

    /***
     * 获取课程的下拉框
     * @param teacherId
     * @return
     */
    List<Map<String, Object>> getSimpleMapForTeacher(Integer teacherId);

    /**
     * 查询课程有哪些学生
     * @param courseId
     * @return
     */
    List<StudentView> queryCourseStuents(Integer courseId);


    /**
     * 更新课程下面学生的状态
     * @param courseId
     * @param studId
     * @param i
     * @return
     */
    int updateCourseStuent(Integer courseId, Integer studId, int i);

    /***
     * 查询首页的数据
     * @return
     */
    Map<String,Object> queryIndexData();

    /**
     * 查询所有的公开课
     * @return
     */
    List<Course> queryAllOpenCourse();

}

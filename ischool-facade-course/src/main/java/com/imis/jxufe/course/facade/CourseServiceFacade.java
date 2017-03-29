package com.imis.jxufe.course.facade;

import com.imis.jxufe.base.model.SimpleResponse;
import com.imis.jxufe.course.model.Course;

import java.util.List;

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
     boolean addCourse(Course course);


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






}

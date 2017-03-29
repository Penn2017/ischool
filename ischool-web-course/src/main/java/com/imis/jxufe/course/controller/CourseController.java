package com.imis.jxufe.course.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.SimpleResponse;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.course.model.Course;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongping
 * @date 2017/3/29
 */
@RestController
public class CourseController {


    @Resource(name = "courseServiceFacade")
    private CourseServiceFacade courseService;


    /***
     * 创建课程
     * @param course
     * @return
     */
    @RequestMapping("/create")
    public ResponseEntity  createCourse(@ModelAttribute("course") Course course){
        ResponseEntity responseEntity=null;
        boolean success = courseService.addCourse(course);
        if (success) {
            responseEntity = new ResponseEntity(200,"添加课程成功");
        }else{
            responseEntity = new ResponseEntity(400, "添加课程失败");
        }
        return responseEntity;
    }


    /**
     * 删除课程
     * @param courseId
     * @return
     */
    @RequestMapping("/delete/{courseId}")
    public ResponseEntity deleteCourse(@PathVariable("courseId") Integer courseId){


        Course course = new Course();
        course.setId(courseId);

        SimpleResponse simpleResponse = courseService.deleteCourse(course);
        ResponseEntity responseEntity = new ResponseEntity(simpleResponse.getStatus(), simpleResponse.getMsg());

        return responseEntity;

    }


    /**
     * 修改课程
     * @param course
     * @return
     */
    @RequestMapping("/update")
    public ResponseEntity updateCourse(@ModelAttribute("course") Course course){
        ResponseEntity responseEntity=null;
        boolean success = courseService.updateCourse(course);
        if (success) {
            responseEntity = new ResponseEntity(200,"修改成功");
        }else{
            responseEntity = new ResponseEntity(400,"修改失败");
        }
        return responseEntity;
    }


    /**
     * 查询一个教师的所有的课程
     * @param teacherId
     * @return
     */
    @RequestMapping("/queryall/{teacherId}")
    public List<Course> queryCourses(@PathVariable("teacherId") Integer teacherId){
        ResponseEntity responseEntity=null;
        List<Course> courses = courseService.allCourses(teacherId);

        return courses;
    }


}

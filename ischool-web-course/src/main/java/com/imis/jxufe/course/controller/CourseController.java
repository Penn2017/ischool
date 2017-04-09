package com.imis.jxufe.course.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.SimpleResponse;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.base.model.course.SectionNode;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.course.facade.SectionServiceFacade;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import com.imis.jxufe.resource.facade.ResourceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhongping
 * @date 2017/3/29
 */
@RestController
public class CourseController {


    @Resource(name = "courseServiceFacade")
    private CourseServiceFacade courseService;

    @Autowired
    private RedisServiceFacade redisService;

    @Autowired
    private ResourceFacade resourceService;

    @Autowired
    private SectionServiceFacade sectionService;

    /***
     * 创建课程
     * @param course
     * @return
     */
    @RequestMapping("/create")
    public ResponseEntity  createCourse(Course course){
        ResponseEntity responseEntity=null;
        Integer  courseId= courseService.addCourse(course);
        if (courseId!=null&&courseId!=0) {
            //加入到缓存中，以便后面的查看使用。
            redisService.setObject(course.getInviteCode(), course);
            responseEntity = new ResponseEntity(200,"添加课程成功");
            responseEntity.getParams().put("courseId", courseId);
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

        //删除缓存课程
        Course course1 = courseService.selectOneCourseById(String.valueOf(courseId));
        redisService.del(course1.getInviteCode());

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
    public ResponseEntity updateCourse(Course course){
        ResponseEntity responseEntity=null;
        boolean success = courseService.updateCourse(course);
        if (success) {
            //删除缓存课程
            Course course1 = courseService.selectOneCourseById(String.valueOf(course.getId()));
            redisService.del(course1.getInviteCode());

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
    public ResponseEntity queryCourses(@PathVariable("teacherId") Integer teacherId){
        ResponseEntity responseEntity = new ResponseEntity();
        List<Course> courses = courseService.allCourses(teacherId);
        responseEntity.getParams().put("rows", courses);
        return responseEntity;
    }


    @RequestMapping(value = "/queryCourseVideoTree/{courseId}")
    public ResponseEntity queryCourseVideoTree(@PathVariable("courseId") Integer courseId){

        //找出所有的章
        List<SectionNode> sectionNodes = sectionService.queryAllSection(courseId);

        //根据章找出视频
        List<Map<String,Object>> files=resourceService.queryCourseVideoTree(sectionNodes);
        if (files==null||files.size()==0) {
            return  new ResponseEntity(404,"没有任何资源");
        }

        ResponseEntity result = new ResponseEntity(200,"查询成功");
        result.getParams().put("rows", files);
        return result;

    }



}

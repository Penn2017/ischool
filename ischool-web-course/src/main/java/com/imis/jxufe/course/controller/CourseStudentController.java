package com.imis.jxufe.course.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.course.StudentView;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 管理课程中跟学生有关的controller
 * @author zhongping
 * @date 2017/4/1
 */

@RestController
public class CourseStudentController {


    @Autowired
    private CourseServiceFacade courseService;


    /**
     * 教师查询下拉列表
     * @param teacherId
     * @return
     */
    @RequestMapping("/queryTeacherCourses/{teacherId}")
    public ResponseEntity queryTeacherCourses(@PathVariable("teacherId") Integer teacherId){

        if (teacherId==null) {
            return new ResponseEntity(404,"该教师不存在");
        }

         Map<String,Object> idNamesMaps= courseService.getSimpleMapForTeacher(teacherId);

        if (idNamesMaps==null) {
            return new ResponseEntity(405, "没有任何课程");
        }
        ResponseEntity result = new ResponseEntity(405, "没有任何课程");
        result.getParams().put("courses", idNamesMaps);

        return result;

    }


    /**
     * 查询一个课程下面有哪些学生
     * @param courseId
     * @return
     */
    @RequestMapping("/queryCourseStudents/{courseId}")
    public ResponseEntity queryCourseStudents(@PathVariable("courseId") Integer courseId){

        if (courseId==null) {
            return new ResponseEntity(404,"课程不存在");
        }

        // 查询出课程有哪些学生
         List<StudentView> students=courseService.queryCourseStuents(courseId);

        if (students==null||students.size()==0) {
            return new ResponseEntity(405,"课程没有任何的学生申请");
        }

        //返回查询数据
        ResponseEntity responseEntity = new ResponseEntity(200, "查询成功");

        responseEntity.getParams().put("studs", students);
        return responseEntity;

    }


    /**
     * 对一个学生执行通过操作
     * @param courseId
     * @param studId
     * @return
     */
    @RequestMapping("/allowStuToClass/{courseId}/{studId}")
    public ResponseEntity  allowStuToClass(@PathVariable("courseId") Integer courseId,
                                           @PathVariable("studId") Integer studId){

        if (courseId==null) {
            return new ResponseEntity(404,"课程不存在");
        }

        if (studId==null) {
            return new ResponseEntity(405,"学生不存在");
        }

        //执行更新操作
        int result= courseService.updateCourseStuent(courseId,studId,1);
        if (result>0) {
            return new ResponseEntity(200,"更新成功");
        }
        return new ResponseEntity(400,"更新失败，请重试");

    }


    /**
     * 拒绝学生加入课堂
     * @param courseId
     * @param studId
     * @return
     */
    @RequestMapping("/refulseStuToclass/{courseId}/{studId}")
    public ResponseEntity refulseStuToClass(@PathVariable("courseId") Integer courseId,
                                            @PathVariable("studId") Integer studId){
        if (courseId==null) {
            return new ResponseEntity(404,"课程不存在");
        }

        if (studId==null) {
            return new ResponseEntity(405,"学生不存在");
        }


        //执行更新操作
        int result= courseService.updateCourseStuent(courseId,studId,2);
        if (result>0) {
            return new ResponseEntity(200,"已经拒绝该学生加入");
        }
        return new ResponseEntity(400,"操作失败，请重试");

    }

    /**
     * 把学生从课堂中清除
     * @param courseId
     * @param studId
     * @return
     */
    @RequestMapping("/deleteStufromClass/{courseId}/{studId}")
    public ResponseEntity deleteStufromClass(@PathVariable("courseId") Integer courseId,
                                            @PathVariable("studId") Integer studId){
        if (courseId==null) {
            return new ResponseEntity(404,"课程不存在");
        }

        if (studId==null) {
            return new ResponseEntity(405,"学生不存在");
        }

        //执行更新操作
        int result= courseService.updateCourseStuent(courseId,studId,-1);
        if (result>0) {
            return new ResponseEntity(200,"已经删除该学生");
        }
        return new ResponseEntity(400,"操作失败，请重试");

    }


}

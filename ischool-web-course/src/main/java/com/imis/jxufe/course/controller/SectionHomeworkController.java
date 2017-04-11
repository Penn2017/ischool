package com.imis.jxufe.course.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.homework.Homework;
import com.imis.jxufe.base.model.homework.HomeworkAnswer;
import com.imis.jxufe.homework.facade.HomeworkServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhongping
 * @date 2017/4/10
 */

@RestController
public class SectionHomeworkController {

    @Autowired
    private HomeworkServiceFacade homeworkService;



    /**
     * 查询一门课所有的作业
     * @param courseId
     * @return
     */
    @RequestMapping(value = "/queryCourseHomework/{courseId}")
    public ResponseEntity queryCourseHomework(@PathVariable("courseId") Integer courseId) {

        //查询
        List<Homework> homeworks=homeworkService.queryCourseHomework(courseId);

        if (homeworks==null||homeworks.size()==0) {
            return new ResponseEntity(404,"没有任何的作业！");
        }



        ResponseEntity result = new ResponseEntity(200, "查询成功");
        result.getParams().put("rows", homeworks);

        return result;

    }

    /**
     * 创建一个作业
     * @param homework
     * @return
     */
    @RequestMapping(value = "/createCourseHomework")
    public ResponseEntity   createCourseHomework(Homework homework){

        Integer id=homeworkService.createHomework(homework);

        if (id==null||id==0) {
            return new ResponseEntity(400, "创建失败");
        }

        ResponseEntity result = new ResponseEntity(200, "创建成功");
        result.getParams().put("id", id);

        return result;

    }

    /**
     * 教师查询一个homework的详情部分
     * @param homeworkId
     * @return
     */
    @RequestMapping(value = "/queryOneHomework/{homeworkId}")
    public ResponseEntity  queryOneHomework(@PathVariable("homeworkId")Integer homeworkId){


        return null;

    }

    /*********************************************学生部分********************************************/
    /**
     * 给作业提交答案
     * @param answer
     * @return
     */
    @RequestMapping(value = "/commitHomework")
    public ResponseEntity  commitHomework(HomeworkAnswer answer){
       Integer answerId= homeworkService.commitHomework(answer);


        if (answerId==null||answerId==0) {
            return new ResponseEntity(400, "提交失败，请重试");
        }

        ResponseEntity result = new ResponseEntity(200, "提交成功");
        result.getParams().put("id", answerId);

        return result;
    }


    /**
     * 查询我的作业
     * @param stuId
     * @return
     */
    @RequestMapping(value = "/queryMyHomeWork/{stuId}")
    public ResponseEntity queryMyHomeWork(@PathVariable("stuId")Integer stuId){
        //遍历我的课程，找出哪些作业与我相关

        //找出我是否已经提交过了作业，如果已经提交过了，那么找出我提交的作业


        //封装到前台显示


        return null;
    }













}

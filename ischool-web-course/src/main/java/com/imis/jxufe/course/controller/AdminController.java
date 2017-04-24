package com.imis.jxufe.course.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhongping
 * @date 2017/4/24
 */
@RestController
public class AdminController {


    @Autowired
    private CourseServiceFacade courseService;


    /**
     * 获取首页的数据
     * @return
     */
    @RequestMapping("/getIndexData")
    public ResponseEntity getIndexData(){
        //轮播:[],  精品:[], 热门:[]
        Map<String,Object> indexData=courseService.queryIndexData();

        ResponseEntity result=null;
        if (!indexData.isEmpty()) {
            result = new ResponseEntity(200, "查询成功");
            result.getParams().putAll(indexData);

        }else {
            result=new ResponseEntity(400, "暂无数据");
        }

        return result;
    }


    /**
     * 查询全部课程的接口
     * @return
     */
    @RequestMapping("/queryAllOpenCourse")
    public ResponseEntity queryAllOpenCourse(){
        //轮播:[],  精品:[], 热门:[]
        List<Course> courses=courseService.queryAllOpenCourse();

        ResponseEntity result=null;
        if (!courses.isEmpty()) {
            result = new ResponseEntity(200, "查询成功");
            result.getParams().put("rows",courses);

        }else {
            result=new ResponseEntity(400, "暂无数据");
        }

        return result;
    }








}

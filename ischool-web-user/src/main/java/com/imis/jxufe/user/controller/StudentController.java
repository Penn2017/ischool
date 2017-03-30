package com.imis.jxufe.user.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理学生用户的管理 StudentController
 * @author zhongping
 * @date 2017/3/30
 */
@RestController
public class StudentController {


    /**
     * 加入课堂
     * @return
     */
    @RequestMapping("/joinClass/{jionId}")
    public ResponseEntity joinClass(@PathVariable("joinId") String joinId){



        return null;


    }



}

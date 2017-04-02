package com.imis.jxufe.user.controller;

import com.imis.jxufe.base.model.IschoolUser;
import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import com.imis.jxufe.user.facade.UserServiceFacade;
import com.imis.jxufe.user.model.CourseView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 处理学生用户的管理 StudentController
 * @author zhongping
 * @date 2017/3/30
 */
@RestController
public class StudentController {


    @Autowired
    private RedisServiceFacade redisService;

    @Resource(name = "courseService")
    private CourseServiceFacade courseService;

    @Resource(name = "userServiceFacade")
    private UserServiceFacade userService;


    /**
     * 输入邀请码申请加入课堂课堂
     * @return
     */
    @RequestMapping("/queryPrimaryClass/{joinCode}")
    public ResponseEntity queryPrimaryClass(@PathVariable("joinCode") String joinCode){
        ResponseEntity result=null;

        if (StringUtils.isEmpty(joinCode)) {
            return new ResponseEntity(500,"请求出错");
        }

        //根据邀请码找到对应的课堂
        Course course = redisService.getObject(joinCode, Course.class);
        if (course==null) {
            //继续去数据库中查找
            course=courseService.getCourseByInviteId(joinCode);
            if (course==null||course.getInviteCode()==null) {
                result = new ResponseEntity(404,"没有找到课程，请在检查邀请码是否有误！");
                return result;
            }
            //说明不为null，加入redis
            redisService.setObject(joinCode, course);
        }
        //返回查询结果
        result = new ResponseEntity(200,"查询成功");
        Map<String, Object> params = result.getParams();
        params.put("course", course);
        params.put("joinCode",joinCode);


        return result;


    }

    /***
     * 学生申请加入课堂
     * @param joinCode
     * @param stuId
     * @return
     */
    @RequestMapping("/joinClass/{joinCode}/{stuId}")
    public ResponseEntity joinClass(@PathVariable("joinCode") String joinCode,
                                    @PathVariable("stuId")String stuId){

        //查询是否有该课程
        Course course = courseService.getCourseByInviteId(joinCode);

        //没有课程直接返回
        if (course==null) {
            return new ResponseEntity(404, "没有该课程");
        }

        // 查出学生信息
        IschoolUser student = userService.selectOneUser(stuId);
        //没有学生信息直接返回
        if (student==null) {
            return new ResponseEntity(405, "没有该学生");
        }

        //更新学生信息（学生自己选修的课程id，构成为：courseid:type:state,0代表未审核通过，id代表选修的课程数）
        String sStr = student.getClassId();


        boolean notEmpty = StringUtils.isNotEmpty(sStr);

        //拼接
        if (notEmpty) {
            final String[][] isExistId = {null};
            //判断这个课程是否已经申请过了
            Arrays.stream(sStr.split(",")).forEach((e)->{
                String[] mixId = e.split(":");
                if (StringUtils.equals(mixId[0], String.valueOf(course.getId()))) {
                    isExistId[0] =mixId;
                }

            });

            if (isExistId[0]!=null) {
                  //不允许添加
                return new ResponseEntity(405, "您已经申请过此课程，请不要重复申请");
            }

        }

        //将学生加入课堂
        courseService.applyOneCourse(student,course);

        return new ResponseEntity(200,"您已经成功申请，请关注申请进度！");
    }


    /**
     * 查询我的课程列表
     * @param stuId
     * @return
     */
    @RequestMapping("/queryClass/{stuId}")
    public  ResponseEntity queryMyClass( @PathVariable("stuId")String stuId){
        ResponseEntity result=null;
        IschoolUser  user=userService.selectOneUser(stuId);

        if (user==null) {
            return new ResponseEntity(404, "该用户不存在");
        }

        String cids=user.getClassId();

        if (StringUtils.isEmpty(cids)) {
            return new ResponseEntity(405, "没有任何课程");
        }

        String[] split = cids.split(",");

        if (split!=null&&split.length>0) {
            //对结果进行处理
            List<CourseView> myCourses = Stream.of(split).filter((k)->{

                String[] mixId = k.split(":");
                String courseType = mixId[1];
                if (StringUtils.equals(courseType,"2")) {
                    return true;
                }
                return false;

            }).map((e) -> {

                   CourseView courseView = new CourseView();
                   String[] mixId = e.split(":");

                   //查询该课程
                    Course course = courseService.selectOneCourseById(mixId[0]);
                    courseView.setCourseId(course.getId());
                    courseView.setCourseName(course.getName());
                    courseView.setCourseNotice(course.getNotice());
                    courseView.setImageUrl(course.getImageUrl());

                    //设置当前我对于当前课程的状态
                    courseView.setMyState(mixId[2]);
                    return courseView;




            }).collect(Collectors.toList());

            result = new ResponseEntity(200, "查询成功");
            result.getParams().put("courses", myCourses);
            return result;

        }

        return new ResponseEntity(404,"没有选修任何课程");
    }







}

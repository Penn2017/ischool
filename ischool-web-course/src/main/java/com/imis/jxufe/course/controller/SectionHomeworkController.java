package com.imis.jxufe.course.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.imis.jxufe.base.model.Constant;
import com.imis.jxufe.base.model.IschoolUser;
import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.base.model.homework.Homework;
import com.imis.jxufe.base.model.homework.HomeworkAnswer;
import com.imis.jxufe.base.model.homework.StudentHomeWorkView;
import com.imis.jxufe.base.utils.WebUtil;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.facade.SenderMailServiceFacade;
import com.imis.jxufe.homework.facade.HomeworkServiceFacade;
import com.imis.jxufe.param.MailParam;
import com.imis.jxufe.user.facade.UserServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhongping
 * @date 2017/4/10
 */

@RestController
public class SectionHomeworkController {

    @Autowired
    private HomeworkServiceFacade homeworkService;

    @Autowired
    private SenderMailServiceFacade senderMailService;

    @Autowired
    private UserServiceFacade userService;

    @Autowired
    private CourseServiceFacade courseService;




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
     * 查询一一个作业下面所有的已经提交的答案
     * @param homeworkId
     * @return
     */
    @RequestMapping(value = "/queryCourseHomeworkAnswer/{homeworkId}")
    public ResponseEntity queryCourseHomeworkAnswer(@PathVariable("homeworkId") Integer homeworkId) {


        List<HomeworkAnswer> answers=homeworkService.queryHomeWorkAnswer(homeworkId);
        if (answers==null||answers.size()==0) {
            return new ResponseEntity(404,"还没有任何的作业提交");
        }
        ResponseEntity result = new ResponseEntity(200, "查询成功");
        result.getParams().put("rows", answers);
        return result;

    }






        /**
         * 创建一个作业
         * @param homework
         * @return
         */
    @RequestMapping(value = "/createCourseHomework")
    public ResponseEntity   createCourseHomework(Homework homework,@RequestParam("limitDays")Integer limitDays){

        //转化格式  /*为测试准备，后期可以撤销**/
        homework.setImageUrl(WebUtil.changeUrlToIschoolOSS(homework.getImageUrl()));
        homework.setFileurl(WebUtil.changeUrlToIschoolOSS(homework.getFileurl()));

        Integer id=homeworkService.createHomework(homework,limitDays);

        if (id==null||id==0) {
            return new ResponseEntity(400, "创建失败");
        }

        ResponseEntity result = new ResponseEntity(200, "创建成功");
        result.getParams().put("id", id);

        return result;

    }

    /**
     * 教师查询一个作业完成详情
     * @param homeworkId
     * @return
     */
    @RequestMapping(value = "/queryOneHomework/{homeworkId}")
    public ResponseEntity  queryOneHomework(@PathVariable("homeworkId")Integer homeworkId){
        ResponseEntity result=null;

        //查出指定的作业
        Homework homework = homeworkService.querySpyHomeWork(homeworkId);
        String assignId = homework.getAssignId();

        result = new ResponseEntity(200,"查询成功");

        //预期完成人数
        result.getParams().put("completeNum", homework.getCompleteNum());
        //实际完成人数
        result.getParams().put("actullyComplete", homework.getCopleteActullyNum());

        if (StringUtils.isEquals(assignId, Constant.HOMEWORK_ASSIGN_ALL)) {
            //说明该作业是指定给所有人的作业
            result.getParams().put("assignPeople", "所有人");

        }else {
            //指定组长完成作业
            result.getParams().put("assignPeople", "组长");

        }

        List<IschoolUser> stud=  homeworkService.queryHomeWorkUnComplete(homeworkId);

        List<Integer> stuids = stud.stream().map((e) -> e.getId()).collect(Collectors.toList());
        //未完成人员名单
        result.getParams().put("unComplete", stud);

        //简化的studis
        result.getParams().put("unCompleteIds",stuids);

        return result;

    }






    /**
     * 提醒学生提交作业
     * @param stuIds
     * @return
     */
    @RequestMapping(value = "/warnStudent/{homeworkId}")
    public ResponseEntity  queryOneHomework(@PathVariable("homeworkId")Integer homeworkId,
                                            @RequestParam("stuIds")String[] stuIds){
        if (stuIds==null||stuIds.length==0) {
            return new ResponseEntity(400,"没有学生可以提醒！");
        }

        Homework homework = homeworkService.querySpyHomeWork(homeworkId);
        Course course = courseService.selectOneCourseById(String.valueOf(homework.getCourseid()));

        //循环发邮件
        Arrays.stream(stuIds).forEach((e)->{
            IschoolUser student = userService.selectOneUser(e);

            MailParam mailParam = new MailParam();
            mailParam.setTo(student.getEmail());
            mailParam.setSubject("作业提交提醒通知#"+ LocalDateTime.now().getNano());
            mailParam.setContent("<html>尊敬的：<strong>"+student.getName()+"</strong><br/>您所在的课堂【"+course.getName()+"】,老师正在在催您的作业：【"+homework.getHomeworkName()+"】，快点完成提交吧！<br/>"
                                +"请在"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(homework.getCompleteTime())+"前才能提交哦"
                                 +"<img src=\""+Constant.LOG_URL+"\"/><br/>"
                    +"【<b>Join class anytime ,anywhere | ISchool.</b>】</html>");

            senderMailService.send(mailParam);

        });

        return  new ResponseEntity(200,"学生已经收到邮件，请关注作业进度");
    }


    /**
     * 给学生批改作业
     * @param answerId
     * @param state
     * @return
     */
    @RequestMapping(value = "/judgeHomeWork/{answerId}/{state}")
    public ResponseEntity  judgeHomeWork(@PathVariable("answerId")Integer answerId,
                                         @PathVariable("state")Integer state) {
         boolean success=homeworkService.judgeHomeWorkAnswer(answerId, state);

        if (!success) {
            return new ResponseEntity(404, "没有找到指定的作业");
        }else{

            return new ResponseEntity(200, "批阅成功");
        }

    }

    /*********************************************学生部分********************************************/
    /**
     * 给作业提交答案
     * @param answer
     * @return
     */
    @RequestMapping(value = "/commitHomework")
    public ResponseEntity  commitHomework(HomeworkAnswer answer){

        //转换图片格式
        answer.setAnswerFile(WebUtil.changeUrlToIschoolOSS(answer.getAnswerFile()));

        Homework homework=homeworkService.querySpyHomeWork(answer.getHomeworkId());
        if (homework==null) {
            return new ResponseEntity(404, "没有找到指定的作业");
        }

        Date completeTime = homework.getCompleteTime();
        Date currentTime = new Date();
        if (currentTime.after(completeTime)) {
            //已经超过了提交时间，不允许提交了
            return new ResponseEntity(401, "您的作业已经逾期，不允许提交");
        }

        //进行提交功能
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

        List<StudentHomeWorkView> studentHomeworks=new ArrayList();

        //遍历我的课程，找出哪些作业与我相关
        IschoolUser stud = userService.selectOneUser(String.valueOf(stuId));

        String classId = stud.getClassId();

        if (org.apache.commons.lang3.StringUtils.isEmpty(classId)) {
            return new ResponseEntity(400, "您没有任何作业");
        }
        String[] cids = classId.split(",");

        Arrays.stream(cids).forEach((cid)->{
            List<StudentHomeWorkView> shvs=  homeworkService.queryCourseHomeworkOnme(stud.getId(),cid);
            studentHomeworks.addAll(shvs);

        });

        ResponseEntity result = new ResponseEntity(200, "查询成功");

        //封装到前台显示
        result.getParams().put("rows", studentHomeworks);

        return result;
    }













}

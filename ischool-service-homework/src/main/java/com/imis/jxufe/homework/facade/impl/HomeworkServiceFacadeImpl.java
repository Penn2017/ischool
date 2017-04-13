package com.imis.jxufe.homework.facade.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.imis.jxufe.base.model.Constant;
import com.imis.jxufe.base.model.IschoolUser;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.base.model.homework.Homework;
import com.imis.jxufe.base.model.homework.HomeworkAnswer;
import com.imis.jxufe.base.model.homework.StudentHomeWorkView;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.homework.facade.HomeworkServiceFacade;
import com.imis.jxufe.homework.mapper.AnswerMapper;
import com.imis.jxufe.homework.mapper.HomeworkMapper;
import com.imis.jxufe.user.facade.UserServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhongping
 * @date 2017/4/10
 */
@Service("homeworkServiceFacadeImpl")
public class HomeworkServiceFacadeImpl implements HomeworkServiceFacade {


    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private CourseServiceFacade courseService;

    @Autowired
    private UserServiceFacade userServiceFacade;

    private Logger LOGGER = LoggerFactory.getLogger(HomeworkServiceFacadeImpl.class);

    //全局锁
    private Lock lock = new ReentrantLock();// 锁对象

    @Override
    @Transactional(readOnly = true)
    public List<Homework> queryCourseHomework(Integer courseId) {
        Homework homework = new Homework();
        homework.setCourseid(courseId);

        List<Homework> select = homeworkMapper.select(homework);

        return select;
    }


    @Override
    @Transactional
    public Integer createHomework(Homework homework, Integer limitDays) {
        String assignId = homework.getAssignId();
        LOGGER.debug("--------------------------HOMEWORK_ASSIGN_ALL stuNum :"+Constant.HOMEWORK_ASSIGN_ALL);

        if (StringUtils.isEmpty(assignId.trim())) {
            homework.setAssignId(Constant.HOMEWORK_ASSIGN_ALL);
        }
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        //设置完成时间
        calendar.add(Calendar.DATE, limitDays);
        Date completeTime = calendar.getTime();
        homework.setCreateTime(date);

        homework.setCompleteTime(completeTime);


        //设置预期完成人数
        if (StringUtils.isEquals(Constant.HOMEWORK_ASSIGN_ALL, homework.getAssignId())) {
            Course course= courseService.selectOneCourseById(String.valueOf(homework.getCourseid()));
            LOGGER.debug("--------------------------course stuNum :"+course.getStuNum());
            //所有人
            homework.setCompleteNum(course.getStuNum());

        }else {
            homework.setCompleteNum(homework.getAssignId().split(",").length);
        }

        homeworkMapper.insert(homework);

        return homework.getId();
    }

    @Override
    @Transactional
    public Integer commitHomework(HomeworkAnswer answer) {
        //查询是否有我上次提交的作业，如果有，先删除，再提交
        HomeworkAnswer select = new HomeworkAnswer();
        select.setHomeworkId(answer.getHomeworkId());
        select.setAnswerUserId(answer.getAnswerUserId());

        int i = answerMapper.selectCount(select);
        if (i!=0) {
            //说明要删除以前的
            answerMapper.delete(select);
        }


        //提交时间
        answer.setAnswerTime(new Date());
        //设置成待审核
        answer.setState("3");

        //提交作业
        answerMapper.insert(answer);



        lock.lock();// 得到锁
        try {
            //更新完成人数
            Homework homework = homeworkMapper.selectByPrimaryKey(answer.getHomeworkId());
            homework.setCompleteNum(homework.getCompleteNum() + 1);
            //执行更新完成人数
            homeworkMapper.updateByPrimaryKeySelective(homework);

        } finally {
            lock.unlock();// 释放锁
        }

        //返回创建作业的id

        return answer.getId();
    }


    @Override
    public Homework querySpyHomeWork(Integer homeworkId) {
        return homeworkMapper.selectByPrimaryKey(homeworkId);
    }

    @Override
    public List<IschoolUser> queryHomeWorkUnComplete(Integer homeworkId) {

        Homework homework = querySpyHomeWork(homeworkId);
        String assignId = homework.getAssignId();

        List<IschoolUser> unCompleteList = new ArrayList<>();

        if (StringUtils.isEquals(assignId, Constant.HOMEWORK_ASSIGN_ALL)) {
            //说明是所有人

            //查出这门课下面的所有学生
            Course course=
                    courseService.selectOneCourseById(String.valueOf(homework.getCourseid()));

            String str = course.getStuId();
            String[] stuIds = str.split(",");

            Arrays.stream(stuIds).forEach((e)->{
                //解析学生id
                String[] mixIds = e.split(":");

                String stuState = mixIds[1];
                if (StringUtils.isEquals(stuState,"1")) {
                    //如果该学生是通过的状态，那么就说明这个学生也是要做这个作业的。
                    //获取学生的id
                    String sid=mixIds[0];
                    lookForJoin(sid, homeworkId,unCompleteList);

                }
            });


        }else{
            //说明是指定组长的作业
            String[] split = assignId.split(",");
            //遍历这些组长
            Arrays.stream(split).forEach((e)->{
                lookForJoin(e, homeworkId,unCompleteList);

            });
        }

        return unCompleteList;
    }

    /**
     * 寻找，加入未完成作业的学生
     * @param sid
     * @param homeworkId
     */
    private void lookForJoin(String sid, Integer homeworkId,List unCompleteList) {

        HomeworkAnswer answer = new HomeworkAnswer();
        answer.setHomeworkId(homeworkId);
        answer.setAnswerUserId(Integer.valueOf(sid));

        //去答案表里面找这个学生
        HomeworkAnswer select = answerMapper.selectOne(answer);

        if (select==null) {
            //说明没有交作业,加入没有交作业的名单里面
            unCompleteList.add(userServiceFacade.selectOneUser(sid));
        }

    }


    @Override
    public List<StudentHomeWorkView> queryCourseHomeworkOnme(Integer stud, String cid) {

        List<StudentHomeWorkView> thisCourseHomeWork = new ArrayList<>();

        //查出这门课的作业
        Homework homework = new Homework();
        String[] split = cid.split(":");

        if (StringUtils.isEquals(split[2], "1")) {
            //这门课已经是通过状态，才去下面找属于自己的作业
            String classId = split[0];
            homework.setCourseid(Integer.valueOf(classId));
            List<Homework> select = homeworkMapper.select(homework);

            select.stream().forEach((hw)->{
                String assignId = hw.getAssignId();
                if (!StringUtils.isEmpty(assignId)) {
                    if (StringUtils.isEquals(assignId, Constant.HOMEWORK_ASSIGN_ALL)) {
                        StudentHomeWorkView shk=matchHomeworkToview(hw,stud);
                        thisCourseHomeWork.add(shk);
                    }else{
                        //查找里面是否有该名学生
                        if (StringUtils.isContains(assignId, String.valueOf(stud))) {
                            //说明是作业指定人
                            StudentHomeWorkView shk=matchHomeworkToview(hw,stud);
                            thisCourseHomeWork.add(shk);
                        }
                    }

                }


            });
        }



        return thisCourseHomeWork;
    }

    /***
     * 把homework转化为studentHomeWorkview
     * @param hw
     * @param stud
     * @return
     */
    private StudentHomeWorkView matchHomeworkToview(Homework hw, Integer stud) {
        StudentHomeWorkView shv = new StudentHomeWorkView();

        shv.setHomeworkId(hw.getId());
        shv.setHomeworkName(hw.getHomeworkName());
        shv.setHomeworkContent(hw.getHomeworkContent());

        //作业的图片
        shv.setImageUrl(hw.getImageUrl());
        shv.setCourseid(hw.getCourseid());
        shv.setFileurl(hw.getFileurl());

        shv.setCreateTime(hw.getCreateTime());

        //去找对这个h的答案

        HomeworkAnswer answer = new HomeworkAnswer();
        answer.setAnswerUserId(stud);
        answer.setHomeworkId(hw.getId());


        HomeworkAnswer myAnswer = answerMapper.selectOne(answer);

        if (myAnswer==null) {
            //说明没有交作业
            shv.setAnswerState(2);
        }else{

            shv.setAnswerState(Integer.valueOf(myAnswer.getState()));
            shv.setAnswerId(myAnswer.getId());
            shv.setAnswerContent(myAnswer.getAnswerContent());
            shv.setAnswerFile(myAnswer.getAnswerFile());
            shv.setAnswerTime(myAnswer.getAnswerTime());
        }

        return shv;
    }


    @Override
    public boolean judgeHomeWorkAnswer(Integer answerId, Integer state) {
        HomeworkAnswer homeworkAnswer = new HomeworkAnswer();
        homeworkAnswer.setId(answerId);
        homeworkAnswer.setState(String.valueOf(state));
        return answerMapper.updateByPrimaryKeySelective(homeworkAnswer)>0;
    }


    @Override
    public List<HomeworkAnswer> queryHomeWorkAnswer(Integer homeworkId) {
        HomeworkAnswer answer = new HomeworkAnswer();
        answer.setHomeworkId(homeworkId);
        return answerMapper.select(answer);
    }
}

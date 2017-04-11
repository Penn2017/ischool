package com.imis.jxufe.homework.facade.impl;

import com.imis.jxufe.base.model.homework.Homework;
import com.imis.jxufe.base.model.homework.HomeworkAnswer;
import com.imis.jxufe.homework.facade.HomeworkServiceFacade;
import com.imis.jxufe.homework.mapper.AnswerMapper;
import com.imis.jxufe.homework.mapper.HomeworkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
    public Integer createHomework(Homework homework) {
        homework.setCreateTime(new Date());

        homeworkMapper.insert(homework);

        return homework.getId();
    }

    @Override
    @Transactional
    public Integer commitHomework(HomeworkAnswer answer) {


        //提交时间
        answer.setAnswerTime(new Date());
        answer.setState("2");

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
}

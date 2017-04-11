package com.imis.jxufe.homework.facade;

import com.imis.jxufe.base.model.homework.Homework;
import com.imis.jxufe.base.model.homework.HomeworkAnswer;

import java.util.List;

/**
 * @author zhongping
 * @date 2017/4/10
 */
public interface HomeworkServiceFacade {
    /**
     * 查询homework
     * @param courseId
     * @return
     */
    List<Homework> queryCourseHomework(Integer courseId);

    /**
     * 创建一个作业
     * @param homework
     * @return
     */
    Integer createHomework(Homework homework);

    /**
     * 提交作业
     * @param answer
     * @return
     */
    Integer commitHomework(HomeworkAnswer answer);
}

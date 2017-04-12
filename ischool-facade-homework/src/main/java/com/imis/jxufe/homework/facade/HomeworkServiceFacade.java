package com.imis.jxufe.homework.facade;

import com.imis.jxufe.base.model.IschoolUser;
import com.imis.jxufe.base.model.homework.Homework;
import com.imis.jxufe.base.model.homework.HomeworkAnswer;
import com.imis.jxufe.base.model.homework.StudentHomeWorkView;

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
     * @param limitDays
     * @return
     */
    Integer createHomework(Homework homework, Integer limitDays);

    /**
     * 提交作业
     * @param answer
     * @return
     */
    Integer commitHomework(HomeworkAnswer answer);

    /**
     * 查询指定的作业
     * @param homeworkId
     * @return
     */
    Homework querySpyHomeWork(Integer homeworkId);

    /**
     * 查询未完成作业的学生
     * @param homeworkId
     * @return
     */
    List<IschoolUser> queryHomeWorkUnComplete(Integer homeworkId);

    /**
     * 重新这门课跟我相关的作业
     * @param stud
     * @param cid
     * @return
     */
    List<StudentHomeWorkView> queryCourseHomeworkOnme(Integer stud, String cid);

    boolean judgeHomeWorkAnswer(Integer answerId, Integer state);

    /**
     * 查询一个作业下面所有的答案
     * @param homeworkId
     * @return
     */
    List<HomeworkAnswer> queryHomeWorkAnswer(Integer homeworkId);
}

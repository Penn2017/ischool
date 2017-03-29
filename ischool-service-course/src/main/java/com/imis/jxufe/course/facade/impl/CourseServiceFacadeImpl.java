package com.imis.jxufe.course.facade.impl;

import com.imis.jxufe.base.model.SimpleResponse;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.course.model.Course;
import com.imis.jxufe.course.mapper.CourseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程服务接口实现类
 * @author zhongping
 * @date 2017/3/29
 */
@Service("courseServiceFacade")
public class CourseServiceFacadeImpl implements CourseServiceFacade {


    @Autowired
    private CourseMapper courseMapper;

    @Override
    @Transactional
    public boolean addCourse(Course course) {
        if (course!=null) {
            course.setId(null);
            int insert = courseMapper.insert(course);
            return insert > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public SimpleResponse deleteCourse(Course course) {
        SimpleResponse simpleResponse = null;
        //查询该课程下面有没有学生，如果有学生不能删除
        Course c = courseMapper.selectOne(course);
        String stus=c.getStuId();

        if (StringUtils.isNotEmpty(stus)) {
            simpleResponse = new SimpleResponse(400,"该课程下面有学生，不能删除");
        }else{
            courseMapper.deleteByPrimaryKey(course.getId());
            simpleResponse = new SimpleResponse(400,"删除成功");
        }

        return simpleResponse;
    }

    @Override
    @Transactional
    public boolean updateCourse(Course course) {
        if (course!=null&&course.getId()!=null) {
            int i = courseMapper.updateByPrimaryKey(course);
            return i>0;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Course courseDetail(Integer courseId) {
        if (courseId==null) {
            courseId=0;
        }
        return courseMapper.selectByPrimaryKey(courseId);
    }

    @Override
    public List<Course> allCourses(Integer teacherId) {
        Course course = new Course();
        course.setTeacherId(teacherId);
        List<Course> select = courseMapper.select(course);
        if (select==null) {
            select = new ArrayList<>();
        }

        return select;
    }
}

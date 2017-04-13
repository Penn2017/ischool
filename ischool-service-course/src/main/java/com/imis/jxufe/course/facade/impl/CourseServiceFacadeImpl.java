package com.imis.jxufe.course.facade.impl;

import com.imis.jxufe.base.model.IschoolUser;
import com.imis.jxufe.base.model.SimpleResponse;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.base.model.course.StudentView;
import com.imis.jxufe.base.utils.IdWorker;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.course.mapper.CourseMapper;
import com.imis.jxufe.user.facade.UserServiceFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 课程服务接口实现类
 * @author zhongping
 * @date 2017/3/29
 */
@Service("courseServiceFacade")
public class CourseServiceFacadeImpl implements CourseServiceFacade {


    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserServiceFacade userService;


    private static IdWorker idWorker = new IdWorker(10, 10);

    @Override
    @Transactional
    public Integer addCourse(Course course) {
        if (course != null) {
            course.setId(null);
            //判断课程类型
            Integer type = course.getType();
            if (type.intValue() == 2) {
                //生成邀请码
                course.setInviteCode(String.valueOf(idWorker.nextId()));
            }
            course.setCreateTime(new Date());
            course.setStuNum(0);
            int insert = courseMapper.insert(course);
        }
        return course.getId();
    }

    @Override
    @Transactional
    public SimpleResponse deleteCourse(Course course) {
        SimpleResponse simpleResponse = null;
        //查询该课程下面有没有学生，如果有学生不能删除
        Course c = courseMapper.selectOne(course);
        String stus = c.getStuId();

        if (StringUtils.isNotEmpty(stus)) {
            simpleResponse = new SimpleResponse(400, "该课程下面有学生，不能删除");
        } else {
            courseMapper.deleteByPrimaryKey(course.getId());
            simpleResponse = new SimpleResponse(400, "删除成功");
        }

        return simpleResponse;
    }

    @Override
    @Transactional
    public boolean updateCourse(Course course) {
        if (course != null && course.getId() != null) {
            int i = courseMapper.updateByPrimaryKeySelective(course);
            return i > 0;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Course courseDetail(Integer courseId) {
        if (courseId == null) {
            courseId = 0;
        }
        return courseMapper.selectByPrimaryKey(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> allCourses(Integer teacherId) {
        Course course = new Course();
        course.setTeacherId(teacherId);
        List<Course> select = courseMapper.select(course);
        if (select == null) {
            select = new ArrayList<>();
        }

        return select;
    }


    @Override
    @Transactional(readOnly = true)
    public Course getCourseByInviteId(String joinId) {
        Course course = new Course();
        course.setInviteCode(joinId);

        //查询指定课程
        course = courseMapper.selectOne(course);

        return course;
    }

    @Override
    @Transactional(readOnly = true)
    public Course selectOneCourseById(String courseId) {
        return courseMapper.selectByPrimaryKey(Integer.valueOf(courseId));
    }


    @Override
    @Transactional
    public void applyOneCourse(IschoolUser student, Course course) {

        String sNewStr = null;
        //新生成一门课
        Integer courseType = course.getType();
        String applyState = "1";
        if (courseType == 2) {
            applyState = "0";
        }
        String middleStr = course.getId() + ":" + courseType + ":" + applyState;
        ;

        String sStr = student.getClassId();
        //拼接
        if (StringUtils.isEmpty(sStr)) {
            sNewStr = middleStr;
        } else {
            sNewStr = sStr + "," + middleStr;
        }

        student.setClassId(sNewStr);
        //更新
        userService.updateUser(student);
        //更新课程信息（课程下面的学生数，课程选修的人数，构成为：stuId:state,0代表未审核，studId时学生的id）
        String cStr = course.getStuId();
        String cNewStr = null;

        cNewStr = student.getId() + ":" + "0";
        if (cStr != null) {
            cNewStr = cStr + "," + cNewStr;
        }

        course.setStuId(cNewStr);

        //更新课程信息
        updateCourse(course);

    }

    @Override
    @Transactional
    public List<Map<String, Object>> getSimpleMapForTeacher(Integer teacherId) {

        Course course = new Course();
        course.setTeacherId(teacherId);

        List<Map<String, Object>> returnList = new ArrayList<>();

        //查询出结果
        List<Course> select = courseMapper.select(course);


        select.stream().forEach((e) -> {
            Map<String, Object> returnMap = new HashMap();
            returnMap.put("id", String.valueOf(e.getId()));
            returnMap.put("name", e.getName());
            returnList.add(returnMap);
        });
        return returnList;
    }


    @Override
    @Transactional(readOnly = true)
    public List<StudentView> queryCourseStuents(Integer courseId) {

        Course course = courseMapper.selectByPrimaryKey(courseId);
        List<StudentView> returnList=new ArrayList<>();

        //得到选了这门课的学生
        String stuIdsStr=course.getStuId();
        if (StringUtils.isEmpty(stuIdsStr)) {
            //没有学生选了这门课
            return new ArrayList<>();
        }

        //解析
        Arrays.stream(stuIdsStr.split(",")).forEach((e)->{
            String[] mixMeans = e.split(":");
            String stuId=mixMeans[0];

            if (StringUtils.isNotEmpty(stuId)) {
                IschoolUser student = userService.selectOneUser(stuId);

                StudentView studentView = new StudentView(student.getId(),
                        student.getEmail(),
                        student.getName(),
                        student.getImage(),
                        Integer.valueOf(mixMeans[1]));

                studentView.setCourseId(courseId);

                returnList.add(studentView);
            }
        });


        return returnList;
    }

    @Override
    @Transactional
    public int updateCourseStuent(Integer courseId, Integer studId, int i) {
        Course course = courseMapper.selectByPrimaryKey(courseId);
        String stuIdsStr = course.getStuId();

        final String[] newStr = {""};

        Arrays.stream(stuIdsStr.split(",")).forEach((e)->{
            String[] mixMeans = e.split(":");
            String stuId=mixMeans[0];

            if (StringUtils.equals(stuId, String.valueOf(studId))) {
                    e = stuId + ":" + i;

                //同时更新这个学生的课程状态
                IschoolUser ischoolUser = userService.selectOneUser(String.valueOf(studId));
                String classId = ischoolUser.getClassId();
                String[] cids = classId.split(",");

                final String[] newCidStr = {""};

                Arrays.stream(cids).forEach((k)->{
                    String[] mixcids = k.split(":");
                    String cid = mixcids[0];


                    if (cid.equals(String.valueOf(courseId))) {
                        //找到了该们课程，判断
                        k=cid+":"+ i;
                    }

                    if (i!=-1) {
                        //不是删除信号才做添加
                        newCidStr[0] +=k+",";
                    }


                });

                Integer lastApear = newCidStr[0].lastIndexOf(",");
                String finalStr = newCidStr[0].substring(0,lastApear);

                IschoolUser stu = new IschoolUser();
                stu.setId(studId);
                stu.setClassId(finalStr);
                //更新学生状态
                userService.updateUser(stu);


            }
            if (i!=-1) {
                //不是删除信号才做添加
                newStr[0] +=e+",";
            }
        });

        Integer lastApear = newStr[0].lastIndexOf(",");
        String finalStr = newStr[0].substring(0,lastApear);

        course = new Course();
        course.setStuId(finalStr);
        course.setId(courseId);

        //更新学生人数：TODO:检查并发问题
        Integer orginNum=course.getStuNum()==null?0:course.getStuNum();

        course.setStuNum(orginNum+1);

        return courseMapper.updateByPrimaryKeySelective(course);
    }
}


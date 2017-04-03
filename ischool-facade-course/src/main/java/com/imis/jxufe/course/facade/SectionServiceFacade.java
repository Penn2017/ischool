package com.imis.jxufe.course.facade;

import com.imis.jxufe.base.model.course.Section;
import com.imis.jxufe.base.model.course.SectionNode;

import java.util.List;

/**
 * 章节服务的service
 *
 * @author zhongping
 * @date 2017/4/3
 */
public interface SectionServiceFacade {


    /***
     * 添加章节
     * @return
     */
    boolean addSection(Section section);

    /**
     * 删除章节
     * @param sectionId
     * @return
     */
    boolean deleteSection(Integer sectionId);


    /**
     * 修改章节
     * @param section
     * @return
     */
    boolean updateSection(Section section);


    /**
     * 根据课程id查询所有的章节
     * @param courseId
     * @return
     */
    List<SectionNode> queryAllSection(Integer courseId);



}

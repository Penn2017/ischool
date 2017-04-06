package com.imis.jxufe.course.facade.impl;

import com.imis.jxufe.base.model.course.Section;
import com.imis.jxufe.base.model.course.SectionNode;
import com.imis.jxufe.course.facade.SectionServiceFacade;
import com.imis.jxufe.course.mapper.SectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 章节管理实现类
 * @author zhongping
 * @date 2017/4/3
 */
@Service("SectionServiceFacade")
public class SectionServiceFacadeImpl  implements SectionServiceFacade {

    @Autowired
    private SectionMapper sectionMapper;


    @Override
    @Transactional
    public Integer addSection(Section section) {
        section.setCreateTime(new Date());
        sectionMapper.insertSelective(section);
        return section.getId();
    }

    @Override
    @Transactional
    public boolean deleteSection(Integer sectionId) {
         return sectionMapper.deleteByPrimaryKey(sectionId)>0;
    }

    @Override
    @Transactional
    public boolean updateSection(Section section) {
        return sectionMapper.updateByPrimaryKeySelective(section)>0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionNode> queryAllSection(Integer courseId) {

        Section section = new Section();
        section.setCourseId(courseId);

        //找出所有属于这个课程的章/节
        List<Section> select = sectionMapper.select(section);

        List<SectionNode> retrunList = new ArrayList<>();
        //进行章节处理
        select.stream().forEach((e) -> {

            if (e.getParentId()!=null&&e.getParentId()==0) {
                //建立一个章节点
                SectionNode sectionNode = new SectionNode();
                sectionNode.setId(e.getId());
                sectionNode.setName(e.getName());
                sectionNode.setType(1);

                List<SectionNode> rows = new ArrayList<SectionNode>();

                select.stream().forEach((k)->{
                    //找出所有的小节
                    if (k.getParentId()!=null&&k.getParentId()==e.getId()) {
                        SectionNode child = new SectionNode();
                        child.setId(k.getId());
                        child.setName(k.getName());
                        child.setType(2);
                        rows.add(child);
                    }

                });
                //把小节添加到章
                sectionNode.setChildren(rows);
                //把章添加到返回列表中
                retrunList.add(sectionNode);
            }
        });

        return retrunList;
    }
}

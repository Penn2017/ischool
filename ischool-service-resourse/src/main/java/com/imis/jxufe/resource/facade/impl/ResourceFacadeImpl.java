package com.imis.jxufe.resource.facade.impl;

import com.imis.jxufe.base.model.course.SectionNode;
import com.imis.jxufe.base.model.resource.UserFiles;
import com.imis.jxufe.resource.facade.ResourceFacade;
import com.imis.jxufe.resource.mapper.UserFilesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhongping
 * @date 2017/4/3
 */
@Service("ResourceFacadeImpl")
public class ResourceFacadeImpl implements ResourceFacade {

    @Autowired
   private UserFilesMapper userFilesMapper;


    @Override
    @Transactional
    public boolean recordResource(UserFiles userFiles) {

        userFiles.setCreateTime(new Date());

        return  userFilesMapper.insert(userFiles)>0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserFiles> querySectionResources(Integer id, Integer type) {
        UserFiles userFiles=new UserFiles();
        List<UserFiles> select=null;
        if (type==1) {
            //查询小节下面的资源
            userFiles.setSectionId(id);
            select = userFilesMapper.select(userFiles);
        }else{
            //查询课程下面的资源
            userFiles.setCourseId(id);
            List<UserFiles>  temp= userFilesMapper.select(userFiles);
            select= temp.stream().filter((e)->{
                if (e.getSectionId()==null||e.getSectionId()==0) {
                    return true;
                }
               return false;
            }).collect(Collectors.toList());
        }

        return select;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryCourseVideoTree(List<SectionNode> course) {

        if (course==null||course.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> returnList = new ArrayList<>();

        //说明存在资源
        course.stream().forEach((e) -> {
                    Map<String, Object> section = new HashMap();
                    section.put("id", e.getId());
                    section.put("name", e.getName());

                    //处理下面的小节
                    List<Map<String, Object>> nodes = new ArrayList<>();

                    List<SectionNode> children = e.getChildren();

                    if (children != null || children.size() > 0) {
                        children.stream().forEach((k) -> {
                            Map<String, Object> node = new HashMap<>();
                            node.put("id", k.getId());
                            node.put("parentid", e.getId());
                            node.put("name", k.getName());

                            UserFiles userFiles = new UserFiles();
                            userFiles.setSectionId(k.getId());

                            //查出url
                            userFiles = userFilesMapper.selectOne(userFiles);
                            if (userFiles != null) {
                                node.put("url", userFiles.getUrl());
                            } else {
                                node.put("url", "");
                            }
                            nodes.add(node);
                        });
                    }
                    section.put("node", nodes);
                    returnList.add(section);

                }
        );

        return returnList;
    }
}
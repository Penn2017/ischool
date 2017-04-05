package com.imis.jxufe.resource.facade;

import com.imis.jxufe.base.model.course.SectionNode;
import com.imis.jxufe.base.model.resource.UserFiles;

import java.util.List;
import java.util.Map;

/**
 * 资源服务接口
 * @author zhongping
 * @date 2017/4/3
 */
public interface ResourceFacade {


    /**
     * 记录一下用户的类型
     * @param userFiles
     * @return
     */
    boolean recordResource(UserFiles userFiles);

    List<UserFiles> querySectionResources(Integer id, Integer type);

    /**
     * 加载课程-小节-视频数
     * @param courseId
     * @return
     */
    List<Map<String,Object>> queryCourseVideoTree(List<SectionNode> courseId);
}

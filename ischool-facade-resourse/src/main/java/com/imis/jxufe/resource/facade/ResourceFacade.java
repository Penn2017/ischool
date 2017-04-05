package com.imis.jxufe.resource.facade;

import com.imis.jxufe.base.model.resource.UserFiles;

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
}

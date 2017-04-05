package com.imis.jxufe.resource.facade.impl;

import com.imis.jxufe.base.model.resource.UserFiles;
import com.imis.jxufe.resource.facade.ResourceFacade;
import com.imis.jxufe.resource.facade.mapper.UserFilesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
        userFilesMapper.insert(userFiles);
        return false;
    }
}

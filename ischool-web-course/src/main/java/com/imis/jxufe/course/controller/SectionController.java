package com.imis.jxufe.course.controller;

import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.course.Section;
import com.imis.jxufe.base.model.course.SectionNode;
import com.imis.jxufe.base.model.resource.UserFiles;
import com.imis.jxufe.course.facade.SectionServiceFacade;
import com.imis.jxufe.resource.facade.ResourceFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 章节管理
 * @author zhongping
 * @date 2017/3/30
 */
@RestController
public class SectionController {


    @Autowired
    private SectionServiceFacade sectionService;


    @Autowired
    private ResourceFacade resourceService;


    /***
     * 添加章节/小节
     * @return
     */
    @RequestMapping(value = "/section/addSection")
    public ResponseEntity addSection(Section section){

        Integer parentId = section.getParentId();
        if (parentId==null) {
            return new ResponseEntity(405, "请指定章/节");
        }

        Integer flag = sectionService.addSection(section);
        if (flag!=null) {
            ResponseEntity result = new ResponseEntity(200, "添加章节成功");
            result.getParams().put("id", flag);
            return result;
        }
        return new ResponseEntity(400, "操作失败，请重新操作");
    }


    /***
     * 删除章节
     * @return
     */
    @RequestMapping(value = "/section/deleteSection/{sectionId}")
    public ResponseEntity deleteSection(@PathVariable("sectionId")Integer sectionId){
        boolean flag = sectionService.deleteSection(sectionId);
        if (flag) {
            return new ResponseEntity(200, "删除章节成功");
        }
        return new ResponseEntity(400, "操作失败，请重新操作");
    }


    /**
     * 更新章节
     * @return
     */
    @RequestMapping(value = "/section/updateSection")
    public  ResponseEntity updateSection(Section section){
        boolean flag = sectionService.updateSection(section);
        if (flag) {
            return new ResponseEntity(200, "更新章节成功");
        }
        return new ResponseEntity(400, "操作失败，请重新操作");
    }

    /**
     * 查询所有的章节
     * @return
     */
    @RequestMapping(value = "/section/queryAllSections/{courseId}")
    public ResponseEntity queryAllSections(@PathVariable("courseId")Integer courseId){
        List<SectionNode> sectionNodes = sectionService.queryAllSection(courseId);
        if (sectionNodes != null || !(sectionNodes.isEmpty())) {
            ResponseEntity responseEntity = new ResponseEntity(200, "查询成功");
            responseEntity.getParams().put("rows", sectionNodes);
            return responseEntity;
        }

        return new ResponseEntity(404, "没有任何章节");
    }


    /***
     * 记录已经上传的资源
     * @param
     * @return
     */
    @RequestMapping(value = "/section/recordResource")
    public ResponseEntity recordResource(UserFiles userFiles){
        //对url进行解析
        //http://files.jxufe-ischool.top/images/AXP5FyeNCi.png

        //解析URL
        String infos[] = StringUtils.split(userFiles.getUrl(), "/");

        int length = infos.length;
        String fileName = infos[length - 1].trim();
        String path = infos[length - 2].trim();

        String storeStr = "folder=" + path + "&file=" + fileName;
        userFiles.setUrl(storeStr);

        boolean success=resourceService.recordResource(userFiles);
        if (success) {
            return new ResponseEntity(200, "资源记录成功");
        }
        return new ResponseEntity(400, "资源记录失败");
    }


    /**
     * 查询章节/课程下的资源
     * @param id
     * @param type
     * @return
     */
    @RequestMapping(value = "/section/querySectionResources/{id}/{type}")
    public ResponseEntity querySectionResources(@PathVariable("id") Integer id,
                                                @PathVariable("type") Integer type){



        List<UserFiles> files=resourceService.querySectionResources(id,type);
        if (files==null||files.size()==0) {
            return  new ResponseEntity(404,"没有任何资源");
        }

        ResponseEntity result = new ResponseEntity(200,"查询成功");
        result.getParams().put("rows", files);
        return result;

    }



}

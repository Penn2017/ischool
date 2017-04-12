package com.imis.jxufe.course.controller;

import com.aliyun.oss.OSSClient;
import com.imis.jxufe.base.model.ResponseEntity;
import com.imis.jxufe.base.model.SimpleResponse;
import com.imis.jxufe.base.model.course.Course;
import com.imis.jxufe.base.model.course.SectionNode;
import com.imis.jxufe.base.utils.IdWorker;
import com.imis.jxufe.course.facade.CourseServiceFacade;
import com.imis.jxufe.course.facade.SectionServiceFacade;
import com.imis.jxufe.redis.facade.RedisServiceFacade;
import com.imis.jxufe.resource.facade.ResourceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zhongping
 * @date 2017/3/29
 */
@RestController
public class CourseController {


    @Resource(name = "courseServiceFacade")
    private CourseServiceFacade courseService;

    @Autowired
    private RedisServiceFacade redisService;

    @Autowired
    private ResourceFacade resourceService;

    @Autowired
    private SectionServiceFacade sectionService;

    private IdWorker idWorker = new IdWorker(5, 4);

    /***
     * 创建课程
     * @param course
     * @return
     */
    @RequestMapping("/create")
    public ResponseEntity  createCourse(Course course){
        ResponseEntity responseEntity=null;
        Integer  courseId= courseService.addCourse(course);
        if (courseId!=null&&courseId!=0) {
            //加入到缓存中，以便后面的查看使用。
            redisService.setObject(course.getInviteCode(), course);
            responseEntity = new ResponseEntity(200,"添加课程成功");
            responseEntity.getParams().put("courseId", courseId);
        }else{
            responseEntity = new ResponseEntity(400, "添加课程失败");
        }
        return responseEntity;
    }


    /**
     * 删除课程
     * @param courseId
     * @return
     */
    @RequestMapping("/delete/{courseId}")
    public ResponseEntity deleteCourse(@PathVariable("courseId") Integer courseId){


        Course course = new Course();
        course.setId(courseId);

        //删除缓存课程
        Course course1 = courseService.selectOneCourseById(String.valueOf(courseId));
        redisService.del(course1.getInviteCode());

        SimpleResponse simpleResponse = courseService.deleteCourse(course);
        ResponseEntity responseEntity = new ResponseEntity(simpleResponse.getStatus(), simpleResponse.getMsg());



        return responseEntity;

    }


    /**
     * 修改课程
     * @param course
     * @return
     */
    @RequestMapping("/update")
    public ResponseEntity updateCourse(Course course){
        ResponseEntity responseEntity=null;
        boolean success = courseService.updateCourse(course);
        if (success) {
            //删除缓存课程
            Course course1 = courseService.selectOneCourseById(String.valueOf(course.getId()));
            redisService.del(course1.getInviteCode());

            responseEntity = new ResponseEntity(200,"修改成功");
        }else{
            responseEntity = new ResponseEntity(400,"修改失败");
        }
        return responseEntity;
    }


    /**
     * 查询一个教师的所有的课程
     * @param teacherId
     * @return
     */
    @RequestMapping("/queryall/{teacherId}")
    public ResponseEntity queryCourses(@PathVariable("teacherId") Integer teacherId){
        ResponseEntity responseEntity = new ResponseEntity();
        List<Course> courses = courseService.allCourses(teacherId);
        responseEntity.getParams().put("rows", courses);
        return responseEntity;
    }


    @RequestMapping(value = "/queryCourseVideoTree/{courseId}")
    public ResponseEntity queryCourseVideoTree(@PathVariable("courseId") Integer courseId){

        //找出所有的章
        List<SectionNode> sectionNodes = sectionService.queryAllSection(courseId);

        //根据章找出视频
        List<Map<String,Object>> files=resourceService.queryCourseVideoTree(sectionNodes);
        if (files==null||files.size()==0) {
            return  new ResponseEntity(404,"没有任何资源");
        }

        ResponseEntity result = new ResponseEntity(200,"查询成功");
        result.getParams().put("rows", files);
        return result;

    }


    /**
     * 上传文件接口
     * @param request
     * @throws Exception
     */
    @RequestMapping(value="/upfiles",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity upfiles(HttpServletRequest request) throws Exception{
        /***********************************************************/
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com/";
        String accessKeyId = "LTAI4TPzfMR4o0ic";
        String accessKeySecret = "eWjyDSHLQFSa6vA6tSoCWXSoFed0WC";
        /***********************************************************/

        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;

            Iterator<String> iter=multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile imageFile = multiRequest.getFile(iter.next().toString());//(String) iter.next()
                byte[] img = imageFile.getBytes();

                String orginFileName = imageFile.getOriginalFilename();
                String extension=getExtension(orginFileName);

                String path=null;
                if (extension.contains("jpg")||extension.contains("png")) {
                    path = "images";
                } else if (extension.contains("doc") || extension.contains("txt")) {
                    path = "files";
                }else{
                    path = "videos";
                }


                //构造新的路径，新的名称
                String newName = path + "/" + idWorker.nextId() + extension;

                String filename = "" +".png";
                ossClient.putObject("ischool2017", filename, new ByteArrayInputStream(img));
                URL url = ossClient.generatePresignedUrl("ischool2017", newName, expiration);

                ossClient.shutdown();
                ResponseEntity result = new ResponseEntity(200,"上传成功");
                result.getParams().put("fileUrl", url.toString());
                return result;
            }
        }

        return new ResponseEntity(400,"上传失败");
    }


    /**
     * 获取扩展名如 .jpg
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
         String DOT = ".";
         String SLASH_ONE = "/";
         String SLASH_TWO = "\\";

        if (org.apache.commons.lang3.StringUtils.INDEX_NOT_FOUND == org.apache.commons.lang3.StringUtils.indexOf(fileName, DOT))
            return org.apache.commons.lang3.StringUtils.EMPTY;
        String ext = org.apache.commons.lang3.StringUtils.substring(fileName,
                org.apache.commons.lang3.StringUtils.lastIndexOf(fileName, DOT));
        return org.apache.commons.lang3.StringUtils.trimToEmpty(ext);
    }


}

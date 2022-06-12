package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.service.ResourceService;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
@CrossOrigin
public class ResourceController extends BaseController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/v1.0/public/resourceType")
    //获取资源类型
    public Result getResourceType(){
        return resourceService.getResourceType();
    }

    @GetMapping("/v1.0/public/queryRelated")
    public Result queryRelated(@RequestParam Map<String, Object> resourceIDMap){
        return resourceService.queryRelated(resourceIDMap);
    }

    @GetMapping("/v1.0/public/queryResource")
    public Result queryResource(@RequestParam Map<String, Object> resourceIDMap){
        return resourceService.queryResource(resourceIDMap);
    }

    @PostMapping("/v1.0/public/updateRelatedResource")
    public Result updateRelatedResource(){
        return resourceService.updateRelatedResource();
    }

    @GetMapping("/v1.0/public/recommendResource")
    public Result recommendResource(@RequestParam Map<String, Object> IDMap){
        return resourceService.recommendResource(IDMap);
    }

    @GetMapping("/v1.0/public/relatedResource")
    public Result relatedResource(@RequestParam Map<String, Object> resourceIDMap){
        return resourceService.relatedResource(resourceIDMap);
    }

    @GetMapping("/v1.0/public/queryHot")
    public Result queryHot(){
        return resourceService.queryHot();
    }

    @GetMapping("/v1.0/public/queryTime")
    public Result queryTime(){
        return resourceService.queryTime();
    }

    @GetMapping("/v1.0/public/getRotationChart")
    public Result getRotationChart(){
        return resourceService.getRotationChart();
    }

    @GetMapping("/v1.0/public/queryMoreHot")
    public Result queryHotNew(){
        return resourceService.queryMoreHot();
    }

    @GetMapping("/v1.0/public/queryMoreTime")
    public Result queryTimeNew(){
        return resourceService.queryMoreTime();
    }
}

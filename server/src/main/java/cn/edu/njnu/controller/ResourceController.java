package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.service.ResourceService;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/resourceType")
    //获取资源类型
    public Result getResourceType(){
        return resourceService.getResourceType();
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/conditionalQueryResource")
    public Result conditionalQueryResource(@RequestParam Map<String, Object> conditionalMap){
        System.out.println(conditionalMap);
        return resourceService.conditionalQueryResource(conditionalMap);
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/queryRelated")
    public Result queryRelated(@RequestParam Map<String, Object> resourceIDMap){
        System.out.println(resourceIDMap);
        return resourceService.queryRelated(resourceIDMap);
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/queryResource")
    public Result queryResource(@RequestParam Map<String, Object> resourceIDMap){
        return resourceService.queryResource(resourceIDMap);
    }

    @CrossOrigin
    @PostMapping("/v1.0/public/updateRelatedResource")
    public Result updateRelatedResource(){
        return resourceService.updateRelatedResource();
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/recommendResource")
    public Result recommendResource(@RequestParam Map<String, Object> IDMap){
        return resourceService.recommendResource(IDMap);
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/condition")
    public Result condition(){
        return resourceService.selectCondition();
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/relatedResource")
    public Result relatedResource(@RequestParam Map<String, Object> resourceIDMap){
        return resourceService.relatedResource(resourceIDMap);
    }
}

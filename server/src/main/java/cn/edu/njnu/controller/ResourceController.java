package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/e-resource/api/public")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @CrossOrigin
    @GetMapping("/v1.0/getResourceType")
    //获取资源类型
    public Result getResourceType(){
        return resourceService.getResourceType();
    }

    @CrossOrigin
    @GetMapping("/v1.0/conditionalQueryResource")
    public Result conditionalQueryResource(@RequestParam Map<String, Object> conditionalMap){
        return resourceService.conditionalQueryResource(conditionalMap);
    }


}

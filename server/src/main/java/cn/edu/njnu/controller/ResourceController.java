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
    @GetMapping("/conditionalQueryResource")
    public Result conditionalQueryResource(@RequestParam Map<String, Object> conditionalMap){
        return resourceService.conditionalQueryResource(conditionalMap);
    }


}

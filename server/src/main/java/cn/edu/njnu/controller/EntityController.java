package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.EntityService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class EntityController {

    private final EntityService entityService;
    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/relatedEntity")
    public Result relatedEntity(@RequestParam Map<String, Object> keywordMap){
        return entityService.getRelatedEntity(keywordMap);
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/queryEntity")
    public Result queryEntity(@RequestParam Map<String, Object> keywordMap){
        return entityService.queryEntity(keywordMap);
    }

    @GetMapping("/v1.0/private/userGraph/{userID}")
    public Result userGraph(@PathVariable(name = "userID") int userID){
        return entityService.userGraph(userID);
    }
}

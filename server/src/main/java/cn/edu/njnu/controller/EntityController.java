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
    public Result getEntity(@RequestParam Map<String, Object> keywordMap){
        return entityService.getEntity(keywordMap);
    }
//    //根据entity查找重难点,从neo4j里面查
//    @CrossOrigin
//    @GetMapping("/v1.0/public/queryEntity_neo4j")
//    public Result getEntity_neo4j(@RequestParam Map<String, Object> keywordMap){
//        return entityService.getEntity_neo4j(keywordMap);
//    }

    @CrossOrigin
    @GetMapping("/v1.0/public/getProperties")
    public Result getProperties(@RequestParam Map<String, Object> nameMap){
        return entityService.getProperties(nameMap);
    }
}

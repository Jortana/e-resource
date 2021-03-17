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
    public Result getEntity(@RequestParam Map<String, Object> keywordMap){
        return entityService.getEntityByKeyword(keywordMap);
    }
}

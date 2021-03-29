package cn.edu.njnu.controller;

import cn.edu.njnu.Reporsitory.RelationRepository;
import cn.edu.njnu.pojo.EntityNode;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @CrossOrigin
    @GetMapping("/v1.0/public/getProperties")
    public Result getProperties(@RequestParam Map<String, Object> nameMap){
        return entityService.getProperties(nameMap);
    }
//    @Autowired
//    private RelationRepository relationRepository;
//
//    @RequestMapping(method = RequestMethod.GET, path = "/rest/v1/person")
//    public List<EntityNode> getMoviesByPersonName(@RequestParam String name) {
//        return relationRepository.findUserRelationByEachId(name);
//    }
}

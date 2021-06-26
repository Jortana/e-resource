package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/v1.0/private/folder")
    public Result favorite(){
        return favoriteService.favorite();
    }

    @PostMapping("/v1.0/private/createFolder")
    public Result createFolder(@RequestBody Map<String, Object> infoMap){
        System.out.println(infoMap);
        return favoriteService.createFolder(infoMap);
    }
}

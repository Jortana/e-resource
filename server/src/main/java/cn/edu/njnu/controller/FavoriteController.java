package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.FavoriteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/v1.0/public/favorite")
    public Result favorite(@RequestParam Map<String, Object> userIDMap){
        return favoriteService.favorite(userIDMap);
    }
}

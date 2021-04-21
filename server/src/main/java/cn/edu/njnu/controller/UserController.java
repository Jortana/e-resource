package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.pojo.User;
import cn.edu.njnu.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/v1.0/user/{username}")
    public Result updateUser (@PathVariable(name = "username") String username, @RequestBody User requestUser) {
        requestUser.setUsername(username);
        return userService.modifyUserInfo(requestUser);
    }

    @CrossOrigin
    @PostMapping("/v1.0/public/relatedUser")
    public Result relatedUser(){
        return userService.relatedUser();
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/recommendUser")
    public Result recommend(@RequestParam Map<String, Object> userIDMap){
        return userService.recommend(userIDMap);
    }
}

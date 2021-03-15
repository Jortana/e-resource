package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.pojo.User;
import cn.edu.njnu.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping("/e-resource/api")
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/v1.0/public/register")
    public Result register(@RequestBody User user) {
        System.out.println(user);
        String username = user.getUsername();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        if (userService.register(user)) {
            return ResultFactory.buildSuccessResult("注册成功", null);
        } else {
            return ResultFactory.buildFailResult("用户名或邮箱已被占用");
        }
    }
}

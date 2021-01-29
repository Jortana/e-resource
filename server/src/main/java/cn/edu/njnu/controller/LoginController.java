package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/e-resource/api")
public class LoginController {

    @PostMapping("/v1.0/open/login")
    public Result login(@RequestBody User requestUser) {
        System.out.println(requestUser);
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getUserPassword());
        try {
            subject.login(usernamePasswordToken);
            return ResultFactory.buildSuccessResult("登录成功", username);
        } catch (AuthenticationException e) {
            return ResultFactory.buildFailResult("账号密码错误");
        }
    }
}

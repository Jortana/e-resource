package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/e-resource/api")
public class AuthenticationController {
    @GetMapping("/v1.0/private/authentication")
    public Result authentication(){
        String message = "身份认证成功";
        return ResultFactory.buildSuccessResult(message, null);
    }
}

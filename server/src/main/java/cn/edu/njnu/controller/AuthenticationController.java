package cn.edu.njnu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/e-resource/api")
public class AuthenticationController {
    @GetMapping("/authentication")
    public String authentication(){
        return "身份认证成功";
    }
}

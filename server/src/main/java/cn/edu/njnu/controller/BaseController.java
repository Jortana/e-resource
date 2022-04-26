package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.ResultCode;
import cn.edu.njnu.pojo.ResultFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    //定义ExceptionHandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        System.out.println(ex);
        return ResultFactory.buildResult(ResultCode.INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR",null);
    }
}

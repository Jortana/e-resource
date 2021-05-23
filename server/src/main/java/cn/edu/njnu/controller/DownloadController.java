package cn.edu.njnu.controller;


import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.DownloadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/e-resource/api")
public class DownloadController {
    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @RequestMapping("/v1.0/public/download")
    public Result downloadFile(@RequestParam Map<String, Object> resourceIDMap, final HttpServletResponse response, final HttpServletRequest request){
        return downloadService.downloadFile(resourceIDMap,response,request);
    }
}

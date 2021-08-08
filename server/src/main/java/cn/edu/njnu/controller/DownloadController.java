package cn.edu.njnu.controller;


import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.DownloadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
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

    @GetMapping("/v1.0/public/downloadFolder")
    public Result downloadFolder(@RequestParam Map<String, Object> folderIDMap) throws FileNotFoundException {
        System.out.println(folderIDMap);
        return downloadService.downloadFolder(folderIDMap);
    }
}

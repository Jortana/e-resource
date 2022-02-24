package cn.edu.njnu.controller;


import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.DownloadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.Map;

@RestController
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

    @PostMapping("/v1.0/private/downloadFolder")
    public Result downloadFolder(@RequestBody Map<String, Object> folderIDMap) throws FileNotFoundException {
        return downloadService.downloadFolder(folderIDMap);
    }
}

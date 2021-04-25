package cn.edu.njnu.controller;

import cn.edu.njnu.service.RecordService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class RecordController {
    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/v1.0/private/record")
    public void addRecord(@RequestBody Map<String, Object> recordMap){
        recordService.addRecord(recordMap);
    }
}

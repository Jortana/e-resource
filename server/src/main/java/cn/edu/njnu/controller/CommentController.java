package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/queryComment")
    public Result queryComment(@RequestParam Map<String, Object> resourceIDMap){
        return commentService.queryComment(resourceIDMap);
    }
}

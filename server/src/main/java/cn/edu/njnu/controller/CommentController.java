package cn.edu.njnu.controller;

import cn.edu.njnu.pojo.Comment;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/e-resource/api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @CrossOrigin
    @GetMapping("/v1.0/public/comment")
    public Result comment(@RequestParam Map<String, Object> resourceIDMap){
        return commentService.comment(resourceIDMap);
    }

    @CrossOrigin
    @PostMapping("/v1.0/public/addComment")
    public Result addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}

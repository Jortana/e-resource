package cn.edu.njnu.service;

import cn.edu.njnu.mapper.CommentMapper;
import cn.edu.njnu.pojo.Comment;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Map;

@Service
public class CommentService {
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public Result queryComment(@RequestParam Map<String, Object> resourceIDMap){
        int resourceID = Integer.parseInt((String) resourceIDMap.get("resourceID"));
        ArrayList<Comment> commentArrayList = commentMapper.queryComment(resourceID);
        return ResultFactory.buildSuccessResult("获取评论成功", commentArrayList);
    }
}

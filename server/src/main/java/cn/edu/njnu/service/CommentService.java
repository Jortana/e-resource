package cn.edu.njnu.service;

import cn.edu.njnu.mapper.CommentMapper;
import cn.edu.njnu.pojo.Comment;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import scala.collection.immutable.Nil;

import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class CommentService {
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public Result comment(Map<String, Object> resourceIDMap){
        int resourceID = Integer.parseInt((String) resourceIDMap.get("resourceID"));
        ArrayList<Comment> commentArrayList = commentMapper.queryComment(resourceID);
        return ResultFactory.buildSuccessResult("获取评论成功", commentArrayList);
    }

    public Result addComment(Comment comment){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String addDate = formatter.format(date);
        comment.setDate(addDate);
        commentMapper.addComment(comment);
        return ResultFactory.buildSuccessResult("评论添加成功", null);
    }
}

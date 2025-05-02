package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.CommentService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/getCommentList")
    public ApiResponse getCommentList(@RequestBody Map<String,Integer> requestBody){
        Integer articleId = requestBody.get("articleId");
        return commentService.getCommentList(articleId);
    }

    @PostMapping("/addComment")
    public ApiResponse addComment(@RequestBody Map<String,Object> requestBody, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = claims.get("id", Integer.class);

        Integer articleId = (Integer)requestBody.get("articleId");
        String commentContent = (String)requestBody.get("commentContent");
        Integer comParentId = (Integer)requestBody.get("comParentId");

        return commentService.addComment(userId,articleId,commentContent,comParentId,request);
    }

    @PostMapping("/getCommentCount")
    public ApiResponse getCommentCount(){
        return commentService.getCommentCount();
    }
}

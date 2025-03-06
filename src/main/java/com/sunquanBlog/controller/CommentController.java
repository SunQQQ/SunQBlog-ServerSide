package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}

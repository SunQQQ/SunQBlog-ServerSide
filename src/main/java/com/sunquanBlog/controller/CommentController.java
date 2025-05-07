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

    /*
        * @Description: 用户端，根据文章id获取对应评论列表，不分页，二级列表展示
     */
    @PostMapping("/getCommentList")
    public ApiResponse getCommentList(@RequestBody Map<String,Integer> requestBody){
        Integer articleId = requestBody.get("articleId");
        return commentService.getCommentList(articleId);
    }

    /*
        * @Description: 管理端，获取所有文章的评论，分页
     */
    @PostMapping("/getAdminList")
    public ApiResponse getAdminList(@RequestBody Map<String,Integer> requestBody){
        Integer start = requestBody.get("start");
        Integer pageSize = requestBody.get("pageSize");

        return commentService.getAdminList(start, pageSize);
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

    @PostMapping("/deleteComment")
    public ApiResponse deleteComment(@RequestBody Map<String,Integer> requestBody){
        Integer id = requestBody.get("id");
        return commentService.deleteComment(id);
    }
}

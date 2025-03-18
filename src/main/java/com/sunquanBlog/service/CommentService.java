package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    public ApiResponse getCommentList(Integer articleId);

    public ApiResponse addComment(Integer userId, Integer articleId, String commentContent, Integer comParentId,String city);

    public ApiResponse getCommentCount();
}

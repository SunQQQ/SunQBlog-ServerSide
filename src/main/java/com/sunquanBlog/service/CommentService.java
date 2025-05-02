package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.model.Comment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface CommentService {
    public ApiResponse getCommentList(Integer articleId);

    public ApiResponse addComment(Integer userId, Integer articleId, String commentContent, Integer comParentId, HttpServletRequest request);

    public ApiResponse getCommentCount();
}

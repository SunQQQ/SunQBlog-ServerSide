package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.CommentMapper;
import com.sunquanBlog.model.Comment;
import com.sunquanBlog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    public ApiResponse getCommentList(Integer articleId) {
        List<Comment> comment = commentMapper.getCommentList(articleId);

        return ApiResponse.success(comment);
    }
}

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

    public ApiResponse getAdminList(Integer start, Integer pageSize,Integer createId);

    public ApiResponse deleteComment(Integer id);

    public ApiResponse updateComment(Integer id, String commentContent, String city); // 更新评论内容和城市信息
}

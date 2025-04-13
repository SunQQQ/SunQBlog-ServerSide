package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.CommentMapper;
import com.sunquanBlog.model.Comment;
import com.sunquanBlog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunquanBlog.service.LogService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    LogService logService;
    public ApiResponse getCommentList(Integer articleId) {
        List<Comment> comment = commentMapper.getCommentList(articleId);

        return ApiResponse.success(comment);
    }

    public ApiResponse addComment(Integer userId, Integer articleId, String commentContent, Integer comParentId, String city, HttpServletRequest request) {
        Integer num = commentMapper.addComment(userId, articleId, commentContent, comParentId,city);
        if(num == 1){
            // 记录日志
            logService.createLog(request,"用户端", "文章详情页", "创建" , "评论",  "：" + commentContent);
            return ApiResponse.success("评论成功");
        }else {
            return ApiResponse.error(500,"评论失败");
        }
    }

    public ApiResponse getCommentCount() {
        Integer count = commentMapper.getCommentCount();
        return ApiResponse.success(count);
    }
}

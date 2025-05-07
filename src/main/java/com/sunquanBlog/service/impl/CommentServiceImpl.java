package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.CommentMapper;
import com.sunquanBlog.model.Comment;
import com.sunquanBlog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunquanBlog.service.LogService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    LogService logService;
    public ApiResponse getCommentList(Integer articleId) {
        // 一级评论数据
        List<Comment> comment1 = commentMapper.getCommentList1(articleId);

        // 获取一级评论的id
        List<Integer> comment1Id = new ArrayList<>();
        for (Comment comment : comment1) {
            comment1Id.add(comment.getId());
        }

        if(comment1Id.size() == 0){
            return ApiResponse.success(comment1);
        }

        // 用一级评论的id获取二级评论数据
        List<Comment> comment2 = commentMapper.getCommentList2(comment1Id);

        // 将二级评论的父id与一级评论的id进行匹配
        for (Comment comment1Item : comment1) {
            List<Comment> child = new ArrayList<>();
            for (Comment comment2Item : comment2) {
                if (comment1Item.getId().equals(comment2Item.getCommentParentId())) {
                    child.add(comment2Item);
                }
            }
            comment1Item.setChild(child);
        }

        return ApiResponse.success(comment1);
    }

    public ApiResponse addComment(Integer userId, Integer articleId, String commentContent, Integer comParentId, HttpServletRequest request) {
        String city = logService.getLocation(request);

        Integer num = commentMapper.addComment(userId, articleId, commentContent, comParentId,city);
        if(num == 1){
            // 记录日志
            logService.createLog(request,"用户端", "文章详情页", "创建" , "评论",  "：" + commentContent,userId);
            return ApiResponse.success("评论成功");
        }else {
            return ApiResponse.error(500,"评论失败");
        }
    }

    public ApiResponse getCommentCount() {
        Integer count = commentMapper.getCommentCount();
        return ApiResponse.success(count);
    }

    public ApiResponse getAdminList(Integer start, Integer pageSize){
        Map<String, Object> map = new HashMap<>();
        Integer total = commentMapper.getTotalComment();
        List<Comment> list = commentMapper.getAdminList(start,pageSize);
        map.put("total",total);
        map.put("list",list);

        return ApiResponse.success(map);
    }

    public ApiResponse deleteComment(Integer id){
        Integer num = commentMapper.deleteComment(id);
        if(num == 1){
            return ApiResponse.success("删除成功");
        }else {
            return ApiResponse.error(500,"删除失败");
        }
    }

    public ApiResponse updateComment(Integer id, String commentContent, String city){
        Integer num = commentMapper.updateComment(id, commentContent, city);
        if(num == 1){
            return ApiResponse.success("更新成功");
        }else {
            return ApiResponse.error(500,"更新失败");
        }
    }
}

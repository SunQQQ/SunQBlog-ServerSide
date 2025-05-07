package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List <Comment> getCommentList1(Integer articleId);

    List <Comment> getCommentList2(List<Integer> commentIds);

    Integer addComment(Integer createrId, Integer articleId, String commentContent, Integer commentParentId,String city);

    Integer getCommentCount();

    Integer getTodayComment();

    Integer getTotalComment();

    List<Comment> getAdminList(Integer start,Integer pageSize);
}

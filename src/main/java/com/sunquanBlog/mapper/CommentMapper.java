package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List <Comment> getCommentList(Integer articleId);

    Integer addComment(Integer createrId, Integer articleId, String commentContent, Integer commentParentId,String city);
}

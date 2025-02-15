package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogMapper {
    List<Blog> getBlogList(Integer id,String role);
}

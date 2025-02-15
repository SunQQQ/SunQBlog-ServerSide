package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogMapper {
    List<Blog> getBlogList(Integer userId,String role);

    Blog getBlogDetail(Integer blogId);

    Integer insertBlog(Map<String,Object> params);

    Integer deleteBlog(Integer id);
}

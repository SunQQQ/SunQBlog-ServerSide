package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BlogMapper {
    List<Blog> getBlogList(Integer userId,String role,Integer tagId,Integer start,Integer size);

    List<Blog> getUserBlogList(Integer tagId,Integer start,Integer size);

    Integer getBlogNum(Integer tagId);

    Blog getBlogDetail(Integer blogId);

    Integer insertBlog(Map<String,Object> params);

    Integer deleteBlog(Integer id);

    Integer updateBlog(Map<String,Object> params);

    Integer addBlogViewNum(Integer id);

    Integer getTodayArticle();

    Integer getTotalArticle();
}

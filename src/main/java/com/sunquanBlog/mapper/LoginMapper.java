package com.sunquanBlog.mapper;

import com.sunquanBlog.model.user;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LoginMapper {
    List<user> selectAll();

    List<user> getPassword(String username);

    int register(String username,String password);
}
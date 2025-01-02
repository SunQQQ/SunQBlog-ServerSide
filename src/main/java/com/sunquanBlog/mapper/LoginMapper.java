package com.sunquanBlog.mapper;

import com.sunquanBlog.model.user;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LoginMapper {
    List<user> getAllUser(String role,String username);

    List<user> getPassword(String username);

    int register(String username,String password);

    int deleteByUsername(String username);
}
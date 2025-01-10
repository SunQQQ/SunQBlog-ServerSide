package com.sunquanBlog.mapper;

import com.sunquanBlog.model.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LoginMapper {
    List<User> getAllUser(String role, String username);

    List<User> getPassword(String username);

    int register(String username,String password,String email,String role);

    int deleteByUsername(String username);

    User getUserById(Integer id);
}
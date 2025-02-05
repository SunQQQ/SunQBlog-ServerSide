package com.sunquanBlog.mapper;

import com.sunquanBlog.model.User;
import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface LoginMapper {
    List<User> getAllUser();

    List<User> getPassword(String username);

    int register(String username,String password,String email,String role);

    int deleteByUsername(String username);

    User getUserById(Integer id);

    int updateUser(@Param("params") Map<String,Object> params);
}
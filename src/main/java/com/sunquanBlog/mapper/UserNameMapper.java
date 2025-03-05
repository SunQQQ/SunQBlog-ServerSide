package com.sunquanBlog.mapper;

import com.sunquanBlog.model.UserName;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserNameMapper {
    UserName getOneName();

    Integer markNameAsUsed(Integer id);
}

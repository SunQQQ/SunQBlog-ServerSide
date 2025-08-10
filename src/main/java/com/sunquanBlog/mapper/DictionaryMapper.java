package com.sunquanBlog.mapper;

import com.sunquanBlog.model.Dictionary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictionaryMapper {
    List<Dictionary> getDictionaryList(List<Integer> parentId);
}

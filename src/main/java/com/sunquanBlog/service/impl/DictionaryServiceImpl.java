package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.mapper.DictionaryMapper;
import com.sunquanBlog.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryMapper dictionaryMapper;

    public ApiResponse getDictionaryList(Integer parentId){
        return ApiResponse.success(dictionaryMapper.getDictionaryList(parentId));
    }
}

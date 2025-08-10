package com.sunquanBlog.service;

import com.sunquanBlog.common.util.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DictionaryService {
    public ApiResponse getDictionaryList(List<Integer> parentId);
}

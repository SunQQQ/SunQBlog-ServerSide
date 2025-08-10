package com.sunquanBlog.controller;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @PostMapping("/getDictionaryList")
    public ApiResponse getDictionaryList(@RequestBody Map<String, List<Integer>> requestBody){
        return dictionaryService.getDictionaryList(requestBody.get("parentId"));
    }
}

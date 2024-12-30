package com.sunquanBlog.service.impl;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.user;
import com.sunquanBlog.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private LoginMapper indexMapper;

    public List<user> getAll() {
        return indexMapper.selectAll();
    }
}
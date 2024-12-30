package com.sunquanBlog.service;
import com.sunquanBlog.model.user;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IndexService {

    public List<user> getAll();
}
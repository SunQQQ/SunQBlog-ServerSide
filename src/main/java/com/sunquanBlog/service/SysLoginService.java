package com.sunquanBlog.service;

import com.sunquanBlog.model.user;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public interface SysLoginService {
    public HashMap checkLogin(String username, String password);

    public boolean haveAccount(String username);

    public int register(String username, String password);

    public int deleteByUsername(String username);
}

package com.sunquanBlog.service.impl;

import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.user;
import com.sunquanBlog.service.SysLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private LoginMapper loginMapper;
    public HashMap checkLogin(String username, String password){
        HashMap result = new HashMap<String,String>();

        List<user> list = loginMapper.getPassword(username);
        String tablePassword;
        if(list.size() == 1){
            tablePassword = list.get(0).getPassword();
            System.out.println(tablePassword);

            if(tablePassword.equals(password)){
                result.put("status","success");
            }else{
                result.put("status","failure");
                result.put("version","password error");
            }
        }else {
            result.put("status","failure");
            result.put("version","username error");
        }


        return result;
    }

    public boolean haveAccount(String username){
        List<user> list = loginMapper.getPassword(username);

        if(list.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    public int register(String username, String password){

        int insertNum = loginMapper.register(username,password);

        return insertNum;
    }

    @Override
    public int deleteByUsername(String username) {
        int deleteNum = loginMapper.deleteByUsername(username);
        return deleteNum;
    }
}

package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.JwtUtil;
import com.sunquanBlog.common.util.UserAuthResponse;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.User;
import com.sunquanBlog.service.SysLoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private LoginMapper loginMapper;
    public ApiResponse<UserAuthResponse> checkLogin(String username, String password){
        List<User> list = loginMapper.getPassword(username);
        String tablePassword;
        if(list.size() == 1){
            tablePassword = list.get(0).getPassword();
            JwtUtil jwtUtil = new JwtUtil();
            if(tablePassword.equals(password)){
                User userInfo = list.get(0);
                String token = jwtUtil.generateToken(userInfo);
                UserAuthResponse userAuthResponse = new UserAuthResponse(token, userInfo);
                return ApiResponse.success(userAuthResponse);
            }else{
                return ApiResponse.error(500,"密码错误");
            }
        }else {
            return ApiResponse.error(500,"账号不存在");
        }
    }

    public ApiResponse<String> register(String username, String password,String email,String role){
        // 检查账号是否重复
        List<User> list = loginMapper.getPassword(username);
        boolean haveAccount = list.size() > 0;

        if (haveAccount) {
            return ApiResponse.error(500, "账号已存在，请修改账号");
        } else {
            int insertNum = loginMapper.register(username, password,email,role);
            if (insertNum > 0) {
                return ApiResponse.success("注册成功");
            } else {
                return ApiResponse.error(500, "注册失败请联系博主");
            }
        }
    }

    @Override
    public ApiResponse deleteByUsername(String username) {
        int deleteNum = loginMapper.deleteByUsername(username);

        if (deleteNum == 1) {
            return ApiResponse.success("删除成功");
        } else {
            return ApiResponse.error(500, "删除失败");
        }
    }

    @Override
    public ApiResponse getAllUser(String role,String username) {
        return ApiResponse.success(loginMapper.getAllUser(role,username));
    }

    @Override
    public User getUserById(Integer id) {
        return loginMapper.getUserById(id);
    }
}

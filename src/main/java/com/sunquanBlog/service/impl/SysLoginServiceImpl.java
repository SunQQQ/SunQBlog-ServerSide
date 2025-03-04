package com.sunquanBlog.service.impl;

import com.sunquanBlog.common.util.ApiResponse;
import com.sunquanBlog.common.util.JwtUtil;
import com.sunquanBlog.common.util.UserAuthResponse;
import com.sunquanBlog.mapper.LoginMapper;
import com.sunquanBlog.model.User;
import com.sunquanBlog.service.SysLoginService;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        }else if((list.size() > 1)){
            return ApiResponse.error(500,"此账号存在重复情况");
        }else {
            return ApiResponse.error(500,"账号不存在");
        }
    }

    /**
     * 此接口供管理员使用，需要验证token及role
     * @param username
     * @param password
     * @param email
     * @return
     */
    public ApiResponse<UserAuthResponse> regist(String username, String password,String email){
        // 检查账号是否重复
        List<User> list = loginMapper.getPassword(username);
        boolean haveAccount = list.size() > 0;

        if (haveAccount) {
            return ApiResponse.error(500, "账号已存在，请修改账号");
        } else {
            int insertNum = loginMapper.regist(username, password,email);
            if (insertNum > 0) {
//                return ApiResponse.success("注册成功");
                List<User> listNow = loginMapper.getPassword(username);
                User userInfo = listNow.get(0);

                JwtUtil jwtUtil = new JwtUtil();
                String token = jwtUtil.generateToken(userInfo);

                UserAuthResponse userAuthResponse = new UserAuthResponse(token, userInfo);

                return ApiResponse.success(userAuthResponse);
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
    public ApiResponse getAllUser(Integer userId) {
        User user = getUserById(userId);

        // 无需从body中获取任何信息，直接根据token的id查到该用户的角色
        // 非管理员只返回自己数据，管理员返回所有用户
        if(user.getRole().equals("master")){
            return ApiResponse.success(loginMapper.getAllUser());
        }else{
            List<User> userList = new ArrayList<>();
            userList.add(user);
            return ApiResponse.success(userList);
        }
    }

    @Override
    public User getUserById(Integer id) {
        return loginMapper.getUserById(id);
    }

    @Override
    public ApiResponse updateUser(Map<String,Object> map,Integer accoutId){
        // 该条数据的id
        Integer id = (Integer) map.get("id");

        // 登录id跟修改数据的id不同时，需为管理员
        if(id != accoutId){
            if(!getUserById(accoutId).getRole().equals("master")){
                return ApiResponse.error(500, "无权限进行此操作");
            }
        }

        int updateNum = loginMapper.updateUser(map);

        if (updateNum == 1) {
            return ApiResponse.success("修改成功");
        } else {
            return ApiResponse.error(500, "修改失败");
        }
    }
}

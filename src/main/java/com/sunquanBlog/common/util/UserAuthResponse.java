package com.sunquanBlog.common.util;

import com.sunquanBlog.model.User;

/**
 * 处理用户鉴权相关
 */
public class UserAuthResponse {
    private String token;
    private User userInfo;

    public UserAuthResponse(String token, User userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User userInfo) {
        this.userInfo = userInfo;
    }
}

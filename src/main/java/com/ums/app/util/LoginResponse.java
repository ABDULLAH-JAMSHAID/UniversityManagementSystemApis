package com.ums.app.util;

import com.ums.app.model.User;

import java.util.List;

public class LoginResponse {
    private User user;
    private List<String> roles;
    private String token;
    private String refreshToken;

    public LoginResponse(User user, List<String> roles, String token, String refreshToken) {
        this.user = user;
        this.roles = roles;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}


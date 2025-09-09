package com.ums.app.service;

import com.ums.app.model.User;
import com.ums.app.model.UserRole;
import com.ums.app.util.PasswordUtil;

public class UserService {

    public User registerUser(String userName, String fullName, String email, String password, String role){

        User user=new User();

        String hashPassword= PasswordUtil.hash(password);

        user.setUsername(userName);
        user.setFull_name(fullName);
        user.setEmail(email);
        user.setPassword(hashPassword);
        user.setRole(UserRole.valueOf(role));

        return user;

    }
}

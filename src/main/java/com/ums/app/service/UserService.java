package com.ums.app.service;

import com.ums.app.model.User;
import com.ums.app.model.UserRole;
import com.ums.app.repository.UserRepository;
import com.ums.app.util.JwtUtil;
import com.ums.app.util.LoginResponse;
import com.ums.app.util.PasswordUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final UserRepository userRepository=new UserRepository();

    public User registerUser(String userName, String fullName, String email, String password, String role) throws SQLException {

        User user=new User();

        String hashPassword= PasswordUtil.hash(password);

        user.setUsername(userName);
        user.setFull_name(fullName);
        user.setEmail(email);
        user.setPassword(hashPassword);
        user.setRole(UserRole.valueOf(role));
        int id= userRepository.saveUser(user);
        if (id==-1){
            user.setId(-1);
            return user;
        }
        user.setId(id);
        return user;

    }

    public LoginResponse loginUser(String username,String password) throws SQLException {

        User user=userRepository.findByUsername(username);

        if (user==null){
            return null;
        }
        boolean ok=PasswordUtil.verify(password,user.getPassword());
        if (!ok){
            return null;
        }

        Map <String,Object> claims=new HashMap<>();
        claims.put("roles",user.getRole());
        claims.put("uid",user.getId());
        String token=JwtUtil.generateToken(user.getUsername(),claims);
        return new LoginResponse(token,user);







    }
}

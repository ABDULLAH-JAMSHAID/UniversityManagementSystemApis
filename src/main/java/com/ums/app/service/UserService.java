package com.ums.app.service;

import com.google.gson.JsonDeserializationContext;
import com.ums.app.model.User;
import com.ums.app.repository.UserRepository;
import com.ums.app.util.JwtUtil;
import com.ums.app.util.LoginResponse;
import com.ums.app.util.PasswordUtil;
import jakarta.servlet.http.Cookie;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    private final UserRepository userRepository=new UserRepository();

    public User registerStudent(String userName, String fullName, String email, String password) throws SQLException {

        User user=new User();

        String hashPassword= PasswordUtil.hash(password);

        user.setUsername(userName);
        user.setFull_name(email);
        user.setEmail(email);
        user.setPassword(hashPassword);
        int userId= userRepository.saveUser(user);

        if (userId==-1){
            return null;
        }
        boolean ok=userRepository.saveStudent(userId);
        if (ok){
            userRepository.assignRole(userId,3); // 1 for student
            user.setId(userId);
        }
        return user;

    }

    public User registerTeacher(String userName, String fullName, String email, String password) throws SQLException {

        User user=new User();

        String hashPassword= PasswordUtil.hash(password);

        user.setUsername(userName);
        user.setFull_name(email);
        user.setEmail(email);
        user.setPassword(hashPassword);
        int userId= userRepository.saveUser(user);

        if (userId==-1){
            return null;
        }
        boolean ok=userRepository.saveTeacher(userId);
        if (ok){
            userRepository.assignRole(userId,2); // 1 for teacher
            user.setId(userId);
        }
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

        List<String> roles=userRepository.getUserRoles(user.getId());

        Map <String,Object> claims=new HashMap<>();
        claims.put("uid",user.getId());

        String accessToken = JwtUtil.generateAccessToken(user.getUsername(),claims);
        String refreshToken = JwtUtil.generateRefreshToken(user.getUsername());

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // Production (HTTPS) mein true hoga
        refreshTokenCookie.setPath("/api/auth/refresh"); // Refresh endpoint ka path
        refreshTokenCookie.setMaxAge((int) (JwtUtil.extractExpiration(refreshToken).getTime() - System.currentTimeMillis()) / 1000);

        User user1=new User();
        user1.setId(user.getId());
        user1.setUsername(user.getUsername());

        return new  LoginResponse(user1,roles,accessToken,refreshToken);







    }




}

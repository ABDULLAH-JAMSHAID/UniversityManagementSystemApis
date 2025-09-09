package com.ums.app.controller;
import com.google.gson.Gson;
import com.ums.app.model.User;
import com.ums.app.service.UserService;
import com.ums.app.util.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class UserRegister extends HttpServlet {

    private final Gson gson=new Gson();

    private final UserService userRegisterService=new UserService();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(BufferedReader reader=req.getReader()){

            Map body=gson.fromJson(reader,Map.class);

            String userName =(String) body.get("username");
            String fullName =(String) body.get("full_name");
            String email =(String) body.get("email");
            String password =(String) body.get("password");
            String role = (String) body.get("role");

            if (userName ==null || fullName == null || email == null || password== null || role==null ){
                JsonResponse.badRequest(resp,"Missing Fields");
                return;
            }

            User user =userRegisterService.registerUser(userName.trim(),fullName,email.trim().toLowerCase(),password,role);









        }



    }
}

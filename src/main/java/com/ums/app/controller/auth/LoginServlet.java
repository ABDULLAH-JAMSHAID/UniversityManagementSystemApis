package com.ums.app.controller.auth;
import com.google.gson.Gson;
import com.ums.app.service.UserService;
import com.ums.app.util.JsonResponse;
import com.ums.app.util.LoginResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/auth/login")
public class LoginServlet extends HttpServlet {

    private final Gson gson=new Gson();
    private final UserService userService=new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try(BufferedReader reader=req.getReader()){

            Map body=gson.fromJson(reader,Map.class);

            String username =(String) body.get("username");
            String password =(String) body.get("password");

            if (username ==null || password== null ){
                JsonResponse.badRequest(resp,"Missing Fields");
                return;
            }
            try{
                LoginResponse response =userService.loginUser(username, password);
                if (response==null){
                    JsonResponse.notFound(resp,"Invalid Credentials");
                }else {
                    JsonResponse.ok(resp,response);
                }
            } catch (SQLException e) {
                JsonResponse.serverError(resp,"Database Error");
            }
        }

    }
}
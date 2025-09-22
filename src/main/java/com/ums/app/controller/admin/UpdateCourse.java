package com.ums.app.controller.admin;

import com.google.gson.Gson;
import com.ums.app.service.AdminService;
import com.ums.app.util.JsonResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "UpdateCourse", urlPatterns = "/api/auth/updateCourse/*")
public class UpdateCourse extends HttpServlet {

    private final AdminService adminService=new AdminService();
    private final Gson gson=new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo=req.getPathInfo();

        if (pathInfo==null || pathInfo.equals("/")){
            JsonResponse.notFound(resp,"Please Provide Id In Url");
            return;
        }

        try(BufferedReader reader=req.getReader()){

            Map body=gson.fromJson(reader,Map.class);

            String code =(String) body.get("code");
            String title = (String)body.get("title");
            Double creditHours = (Double) body.get("credit_hours");

            try{
                int id=Integer.parseInt(pathInfo.substring(1));
                boolean ok =adminService.updateCourse(id,code,title,creditHours);
                if (!ok){
                    JsonResponse.notFound(resp,"No Course Found With This Id");
                    return;
                }
                else {
                    JsonResponse.ok(resp,"Course Updated Successfully");
                }
            }catch (SQLException e){
                JsonResponse.badRequest(resp,"Internal Server Error");
            }

        }











    }
}

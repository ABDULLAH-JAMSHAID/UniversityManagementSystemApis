package com.ums.app.controller.admin;

import com.google.gson.Gson;
import com.ums.app.model.Courses;
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
@WebServlet(name = "AddNewCourse" , urlPatterns = "/api/auth/addNewCourse")
public class AddNewCourse extends HttpServlet {

    private final Gson gson=new Gson();

    private final AdminService adminService=new AdminService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(BufferedReader reader=req.getReader()){

            Claims claims=(Claims)req.getAttribute("claims");
            int uid=(int)claims.get("uid");


            Map body=gson.fromJson(reader,Map.class);

            Courses courses=new Courses();

            courses.setCode((String) body.get("code"));
            courses.setTitle((String)body.get("title"));
            courses.setCreditHours((Double)(body.get("credit_hours")));

            if (courses.getCode()==null || courses.getCode().isEmpty() || courses.getTitle()==null || courses.getTitle().isEmpty()){
                JsonResponse.badRequest(resp,"Missing Fields");
                return;
            }
            if ( courses.getCreditHours()<=0 || courses.getCreditHours()>3){
                JsonResponse.badRequest(resp,"Credit Hours Should be between 1 and 3");
                return;
            }

            try{
                boolean ok= adminService.addNewCourse(courses);
                if (!ok){
                    JsonResponse.serverError(resp,"Failed To Add Course");
                }else {
                    JsonResponse.ok(resp,"Course Added Successfully");
                }
            } catch (SQLException e) {
                JsonResponse.serverError(resp,"Course With This Code Already Exists");
            }
        }
    }
}

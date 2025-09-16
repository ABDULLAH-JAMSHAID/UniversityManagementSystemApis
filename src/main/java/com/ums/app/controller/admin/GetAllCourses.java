package com.ums.app.controller.admin;

import com.ums.app.model.Courses;
import com.ums.app.service.AdminService;
import com.ums.app.util.JsonResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GetAllCourses", urlPatterns = "/api/getAllCourses")
public class GetAllCourses extends HttpServlet {

    private final AdminService adminService=new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Claims claims=(Claims)req.getAttribute("claims");
        String role=(String)claims.get("roles");
        int id=(int)claims.get("uid");

        if (role.equals("students") || role.equals("teacher")){
            JsonResponse.forbidden(resp,"Access Denied");
            return;
        }

        List<Courses> courses;
        try {
            courses = adminService.getAllCourses();
            if (courses==null ||courses.isEmpty()){
                JsonResponse.notFound(resp,"Not Found Any Course");
                return;
            }else {
                JsonResponse.ok(resp,courses);
            }
        } catch (SQLException e) {
            JsonResponse.serverError(resp,"Internal Server Error");
        }


    }
}

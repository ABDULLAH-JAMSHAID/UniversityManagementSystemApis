package com.ums.app.controller.admin;

import com.ums.app.annotation.RequiresPermission;
import com.ums.app.model.Courses;
import com.ums.app.model.Permission;
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
public class GetAllCourses extends BaseServlet {

    private final AdminService adminService=new AdminService();

    @Override
    @RequiresPermission(Permission.GET_ALL_COURSES)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Claims claims=(Claims)req.getAttribute("claims");
        int id=(int)claims.get("uid");
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
            throw new ServletException(e);
        }


    }
}

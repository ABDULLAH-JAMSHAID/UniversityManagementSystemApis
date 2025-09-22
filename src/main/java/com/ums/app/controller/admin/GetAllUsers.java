package com.ums.app.controller.admin;

import com.ums.app.model.User;
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
@WebServlet(name = "GetAllUsers", urlPatterns = "/api/auth/getAllUsers/*")
public class GetAllUsers extends HttpServlet {

    private final AdminService adminService=new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Claims claims=(Claims)req.getAttribute("claims");
        int id=(int)claims.get("uid");

        String pathInfo=req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")){
            JsonResponse.badRequest(resp,"Please Provide role in url");
            return;
        }
        String requestedRole=pathInfo.substring(1);
        List<User> user= null;
        try {
            user = adminService.getAllUsers(requestedRole);
        } catch (SQLException e) {
            JsonResponse.serverError(resp,"Internal Server Error");
        }

        if (user==null ||user.isEmpty()){
            JsonResponse.notFound(resp,"Not Found Any Student");
            return;
        }else {
            JsonResponse.ok(resp,user);
        }
    }
}

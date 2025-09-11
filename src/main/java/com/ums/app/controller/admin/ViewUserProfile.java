package com.ums.app.controller.admin;

import com.google.gson.Gson;
import com.ums.app.model.User;
import com.ums.app.service.StudentService;
import com.ums.app.util.JsonResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ViewUserProfile", urlPatterns = {"/api/viewUser/*"})
public class ViewUserProfile extends HttpServlet {

    private final Gson gson=new Gson();
    private final StudentService studentService=new StudentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{

            Claims claims=(Claims)req.getAttribute("claims");

            String roles=(String) claims.get("roles");
            int uid=(int )claims.get("uid");

            String pathInfo = req.getPathInfo(); // e.g. "/5"
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonResponse.badRequest(resp, "Please provide ID in URL");
                return;
            }

            int id;
            try {
                id = Integer.parseInt(pathInfo.substring(1)); // remove "/"
            } catch (NumberFormatException e) {
                JsonResponse.badRequest(resp, "Invalid ID format");
                return;
            }

            if (id!=uid) {
                JsonResponse.forbidden(resp, "You can Only View Your Own Profile");
                return;
            }
            User user=studentService.viewUserProfile(id);
            if (user==null){
                JsonResponse.notFound(resp,"No Such User Found");
                return;
            }
            else {
                JsonResponse.ok(resp,user);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

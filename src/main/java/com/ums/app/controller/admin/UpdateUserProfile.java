package com.ums.app.controller.admin;

import com.google.gson.Gson;
import com.ums.app.model.User;
import com.ums.app.service.StudentService;
import com.ums.app.util.JsonResponse;
import com.ums.app.util.PermissionUtil;
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
@WebServlet(name = "Update User Profile",urlPatterns = "/api/updateUserProfile/*")
public class UpdateUserProfile extends HttpServlet {

    private final Gson gson=new Gson();
    private final StudentService studentService=new StudentService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try(BufferedReader reader=req.getReader()){

            Map body=gson.fromJson(reader, Map.class);

            String username=(String)body.get("username");
            String fullname=(String)body.get("fullname");
            String email=(String)body.get("email");

            Claims claims=(Claims)req.getAttribute("claims");

            int uid=(int )claims.get("uid");


            String pathInfo=req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonResponse.badRequest(resp, "Please Provide ID In URL");
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
                JsonResponse.forbidden(resp, "You can only Update your Own profile");
                return;
            }


            User user=studentService.updateUserProfile(id,username,fullname,email);
            if (user==null){
                JsonResponse.serverError(resp,"Could Not updated Due to Error");
            }
            JsonResponse.ok(resp,"Updated Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

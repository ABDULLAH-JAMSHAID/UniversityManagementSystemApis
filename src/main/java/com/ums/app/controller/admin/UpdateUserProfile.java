package com.ums.app.controller.admin;

import com.google.gson.Gson;
import com.ums.app.annotation.RequiresPermission;
import com.ums.app.model.Permission;
import com.ums.app.model.User;
import com.ums.app.repository.UserRepository;
import com.ums.app.service.StudentService;
import com.ums.app.util.JsonResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
@WebServlet(name = "Update User Profile",urlPatterns = "/api/updateUserProfile/*")
public class UpdateUserProfile extends BaseServlet {

    private final Gson gson=new Gson();
    private final StudentService studentService=new StudentService();
    private final UserRepository userRepository=new UserRepository();

    @Override
    @RequiresPermission(Permission.UPDATE_USER_PROFILE)
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
            if(!userRepository.getUserRoles(uid).contains("ADMIN")){
                if (id!=uid) {
                    JsonResponse.forbidden(resp, "You can only Update your Own profile");
                    return;
                }
            }
            User user=studentService.updateUserProfile(id,username,fullname,email);
            if (user==null){
                JsonResponse.serverError(resp,"Could Not updated Due to Error");
            }
            JsonResponse.ok(resp,"Updated Successfully");

        }
    }
}

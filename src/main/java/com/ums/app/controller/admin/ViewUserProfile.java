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
import java.io.IOException;

@WebServlet(name = "ViewUserProfile", urlPatterns = {"/api/viewUserProfile/*"})
public class ViewUserProfile extends BaseServlet {

    private final Gson gson = new Gson();
    private final StudentService studentService = new StudentService();
    private  final UserRepository userRepository=new UserRepository();

    @Override
    @RequiresPermission(Permission.VIEW_USER_PROFILE)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            // Claims filter se aane chahiye
            Claims claims = (Claims) req.getAttribute("claims");


            if (claims == null) {
                JsonResponse.unauthorized(resp, "Unauthorized: Missing or invalid token");
                return;
            }

            Integer uid = claims.get("uid", Integer.class);

            if (uid == null) {
                JsonResponse.unauthorized(resp, "Invalid token: user id missing");
                return;
            }

            // URL se /{id} nikalna
            String pathInfo = req.getPathInfo(); // e.g. "/5"
            if (pathInfo == null || pathInfo.equals("/")) {
                JsonResponse.badRequest(resp, "Please provide ID in URL");
                return;
            }



               int id = Integer.parseInt(pathInfo.substring(1)); // remove "/"

                JsonResponse.badRequest(resp, "Invalid ID format");



            if(!userRepository.getUserRoles(uid).contains("ADMIN")){
                if (id!=uid) {
                    JsonResponse.forbidden(resp, "You can only Update your Own profile");
                    return;
                }
            }


            // Service se user nikalna
            User user = studentService.viewUserProfile(id);
            if (user == null) {
                JsonResponse.notFound(resp, "No such user found");
            } else {
                JsonResponse.ok(resp, user);
            }
    }
}

package com.ums.app.controller.admin;
import com.google.gson.Gson;
import com.ums.app.service.AdminService;
import com.ums.app.util.JsonResponse;
import com.ums.app.util.PermissionUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "DeleteStudentAndTeacher", urlPatterns = "/api/deleteUser/*")
public class DeleteUser extends HttpServlet {

    private final Gson gson=new Gson();
    private final AdminService adminService=new AdminService();


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Claims claims=(Claims) req.getAttribute("claims");
        int uid=(int)claims.get("uid");

        String pathInfo=req.getPathInfo();

        if (pathInfo==null && pathInfo.equals("/")){
            JsonResponse.badRequest(resp,"Please Provide Id in Url");
            return;
        }
        int id;
        try{
             id=Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            JsonResponse.badRequest(resp,"Please Provide Valid Id In url");
            return;
        }
        if (id==uid){
            JsonResponse.forbidden(resp,"You Can't Delete Your Own Account");
            return;
        }
        try {
            boolean ok=adminService.deleteUser(id);
            if (!ok){
                JsonResponse.notFound(resp,"No Such User Found");
                return;
            }
            JsonResponse.ok(resp,"Deleted Successfully");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

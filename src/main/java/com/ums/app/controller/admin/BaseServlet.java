package com.ums.app.controller.admin;

import com.ums.app.annotation.RequiresPermission;
import com.ums.app.model.Permission;
import com.ums.app.repository.UserRepository;
import com.ums.app.util.JsonResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public abstract class BaseServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String methodName = "do" + req.getMethod().substring(0, 1).toUpperCase()
                    + req.getMethod().substring(1).toLowerCase();


            Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            method.setAccessible(true);

            if (method.isAnnotationPresent(RequiresPermission.class)) {
                RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);
                Permission requiredPermission = annotation.value();

                Claims claims = (Claims) req.getAttribute("claims");
                if (claims == null) {
                    JsonResponse.unauthorized(resp, "Missing token claims.");
                    return;
                }

                Number uid = (Number) claims.get("uid");
                if (uid == null) {
                    JsonResponse.unauthorized(resp, "User ID not found in claims.");
                    return;
                }

                int userId = uid.intValue();

                if (userRepository.userHasPermission(userId, requiredPermission)) {
                    method.invoke(this, req, resp);
                } else {
                    JsonResponse.forbidden(resp, "Access Denied : You Dont Have Permission To Access This Resource");
                }
            } else {
                method.invoke(this, req, resp);
            }

        } catch (NoSuchMethodException e) {
            super.service(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error processing permission check", e);
        }
    }
}

package com.ums.app.controller.admin;

import com.ums.app.annotation.RequiresPermission;

import com.ums.app.handler.AppException;
import com.ums.app.model.Permission;
import com.ums.app.repository.UserRepository;
import com.ums.app.util.JsonResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Build method name (doGet, doPost, etc.)
            String methodName = "do" + req.getMethod().substring(0, 1).toUpperCase()
                    + req.getMethod().substring(1).toLowerCase();

            Method method = this.getClass().getDeclaredMethod(
                    methodName,
                    HttpServletRequest.class,
                    HttpServletResponse.class
            );
            method.setAccessible(true);

            // 🔹 Permission check if annotation exists
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
                    JsonResponse.forbidden(resp, "Access Denied: You don't have permission to access this resource.");
                }
            } else {
                method.invoke(this, req, resp);
            }

        } catch (NoSuchMethodException e) {
            // Default handling for unsupported methods
            super.service(req, resp);

        } catch (InvocationTargetException e) {
            Throwable targetEx = e.getTargetException();
            handleCustomException(targetEx, resp);

        } catch (Exception e) {
            handleCustomException(e, resp);
        }
    }

    // 🔹 Centralized Exception Handler
    private void handleCustomException(Throwable ex, HttpServletResponse resp) throws IOException {
        if (ex instanceof AppException) {
            AppException appEx = (AppException) ex;
            JsonResponse.error(resp, appEx.getStatusCode(), appEx.getMessage());
        } else {
            // fallback for unexpected errors
            JsonResponse.serverError(resp, "Unexpected error: " + ex.getMessage());
        }
    }
}

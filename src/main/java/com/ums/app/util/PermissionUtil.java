package com.ums.app.util;

import com.ums.app.filter.PermissionMapping;
import com.ums.app.model.Permission;
import com.ums.app.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public class PermissionUtil {

    private static final UserRepository userRepository = new UserRepository();

    // check user permission
    public static boolean checkPermission(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Claims AuthFilter me set kiye the
        Claims claims = (Claims) req.getAttribute("claims");

        if (claims == null) {
            JsonResponse.unauthorized(res, "Unauthorized request");
            return false;
        }

        Long userId = claims.get("uid", Long.class);
        String method = req.getMethod();
        String path = req.getRequestURI();

        // Normalize path (replace numbers with {id})
        String normalizedPath = path.replaceAll("/\\d+", "/{id}");

        // Required permission find karo
        Permission requiredPermission = PermissionMapping.getRequiredPermission(method, normalizedPath);

        if (requiredPermission == null) {
            JsonResponse.badRequest(res,"Url is wrong"); // agar mapping hi nahi → default allow
        }

        // User ke permissions fetch from DB
        Set<Permission> userPermissions = userRepository.findPermissionsByUserId(userId);

        if (userPermissions.contains(requiredPermission)) {
            return true; // user allowed
        } else {
            JsonResponse.forbidden(res, "You don't have permission to access this resource");
            return false;
        }
    }
}

package com.ums.app.filter;

import com.ums.app.repository.UserRepository;
import com.ums.app.util.JsonResponse;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/*")
public class RoleFilter implements Filter {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Public endpoints skip
        if (path.endsWith("/api/auth/login")
                || path.endsWith("/api/auth/register/teacher")
                || path.endsWith("/api/auth/register/student")) {
            chain.doFilter(request, response);
            return;
        }

        // AuthFilter se JWT claims aana chahiye
        Object claimsObj = req.getAttribute("claims");
        if (claimsObj == null) {
            JsonResponse.unauthorized(res, "Missing claims. Please login again.");
            return;
        }

        io.jsonwebtoken.Claims claims = (io.jsonwebtoken.Claims) claimsObj;

        // Token me sirf user_id hai
        Integer userId = claims.get("uid", Integer.class);
        if (userId == null) {
            JsonResponse.unauthorized(res, "User ID missing in token.");
            return;
        }

        // User ka role DB se lao
        Integer roleId = userRepository.getRoleIdByUserId(userId);
        if (roleId == null) {
            JsonResponse.unauthorized(res, "User role not found.");
            return;
        }

        // Check karo ke role allowed hai ya nahi
        boolean isAllowed = userRepository.isRoleAllowedForPath(roleId, path);

        if (!isAllowed) {
            JsonResponse.forbidden(res, "You do not have permission to access this resource.");
            return;
        }

        // Allowed -> forward karo
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}
    @Override
    public void destroy() {}
}

package com.ums.app.filter;

import com.ums.app.util.JsonResponse;
import com.ums.app.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter("/api/auth/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.endsWith("/api/auth/login") || path.endsWith("/api/auth/register/teacher") || path.endsWith("/api/auth/register/student")) {
            chain.doFilter(request, response); // login/register skip
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            JsonResponse.badRequest(res, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        if (!JwtUtil.isTokenValid(token)) {
            JsonResponse.unauthorized(res, "Invalid or expired token");
            return;
        }

        try {
            Jws<Claims> jws = JwtUtil.parseToken(token);
            Claims claims = jws.getBody();

            // claims attach kardo request me
            req.setAttribute("claims", claims);

            // ab chain continue karega -> AuthorizationFilter chalega next
            chain.doFilter(request, response);

        } catch (JwtException e) {
            JsonResponse.badRequest(res, "Invalid or expired token");
        }
    }

    @Override public void init(FilterConfig filterConfig) {}
    @Override public void destroy() {}
}

package com.ums.app.filter;

import com.ums.app.util.JsonResponse;
import com.ums.app.util.JwtUtil;
import com.ums.app.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/secure/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            JsonResponse.badRequest(res, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        // Check 1: Token valid hai ya nai
        if (!JwtUtil.isTokenValid(token)) {
            JsonResponse.unauthorized(res, "Invalid or expired token");
            return;
        }

        // Check 2: Token blacklist to nai ho chuka (logout ke bad)
//        if (RedisUtil.isTokenBlacklisted(token)) {
//            JsonResponse.badRequest(res, "Token has been revoked. Please login again.");
//            return;
//        }
        String path = req.getRequestURI();

        if (path.endsWith("/api/auth/login") || path.endsWith("/api/auth/register")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Jws<Claims> jws = JwtUtil.parseToken(token);
            Claims claims = jws.getBody();
            req.setAttribute("claims", claims);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            JsonResponse.badRequest(res, "Invalid or expired token");
        }
    }

    @Override public void init(FilterConfig filterConfig) {}
    @Override public void destroy() {}
}
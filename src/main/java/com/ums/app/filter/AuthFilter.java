package com.ums.app.filter;
import com.ums.app.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();

        if (path.endsWith("/api/auth/login") || path.endsWith("/api/auth/register")) {
            chain.doFilter(request, response);
            return;
        }
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            res.setStatus(401);
            res.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }

        String token = auth.substring(7);

        try {
            Jws<io.jsonwebtoken.Claims> jws = JwtUtil.parseToken(token);
            Claims claims = jws.getBody();
            req.setAttribute("claims", claims);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            res.setStatus(401);
            res.getWriter().write("{\"error\":\"Invalid or expired token\"}");
        }
    }

    @Override
    public void destroy() { }
}

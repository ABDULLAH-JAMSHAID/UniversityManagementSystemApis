package com.ums.app.controller.auth;

import com.ums.app.util.JwtUtil;
import com.ums.app.util.RedisUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@WebServlet("/api/auth/logout")
public class UserLogout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // Refresh token ko Redis mein blacklist karein
        if (refreshToken != null && JwtUtil.isTokenValid(refreshToken)) {
            Date expiryDate = JwtUtil.extractExpiration(refreshToken);
            long remainingMillis = expiryDate.getTime() - System.currentTimeMillis();
            if (remainingMillis > 0) {
                RedisUtil.blacklistToken(refreshToken, TimeUnit.MILLISECONDS.toSeconds(remainingMillis));
            }
        }

        // Optional: Access token ko bhi blacklist kar sakte hain agar woh header se extract karein
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            if (JwtUtil.isTokenValid(accessToken)) {
                Date expiryDate = JwtUtil.extractExpiration(accessToken);
                long remainingMillis = expiryDate.getTime() - System.currentTimeMillis();
                if (remainingMillis > 0) {
                    RedisUtil.blacklistToken(accessToken, TimeUnit.MILLISECONDS.toSeconds(remainingMillis));
                }
            }
        }


        // Client side se cookie clear karein
        Cookie clearCookie = new Cookie("refreshToken", null);
        clearCookie.setHttpOnly(true);
        clearCookie.setPath(req.getContextPath() + "/api/auth/refresh");
        clearCookie.setMaxAge(0);
        resp.addCookie(clearCookie);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Logged out successfully\"}");
    }
}
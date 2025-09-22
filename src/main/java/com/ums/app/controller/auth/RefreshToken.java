package com.ums.app.controller.auth;

import com.google.gson.Gson;
import com.ums.app.model.User;
import com.ums.app.repository.UserRepository;
import com.ums.app.util.JsonResponse;
import com.ums.app.util.JwtUtil;
import com.ums.app.util.RedisUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/auth/refresh")
public class RefreshToken extends HttpServlet {
    private final UserRepository userRepository = new UserRepository();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String refreshToken = extractRefreshToken(req);

        // Check 1: Refresh token cookie mein hai ya nahi?
        if (refreshToken == null) {
            JsonResponse.unauthorized(resp, "Refresh token not found. Please log in again.");
            return;
        }

        // Check 2: Token blacklist to nahi ho chuka (logout ki wajah se)?
        if (RedisUtil.isTokenBlacklisted(refreshToken)) {
            JsonResponse.unauthorized(resp, "Session has been invalidated. Please log in again.");
            return;
        }

        // Check 3: Token expire to nahi ho gaya ya signature theek hai?
        if (!JwtUtil.isTokenValid(refreshToken)) {
            JsonResponse.unauthorized(resp, "Session has expired. Please log in again.");
            return;
        }

        // --- Agar sab checks pass ho gaye, to naya access token banayein ---
        try {
            // 1. Refresh token se username nikalein
            String username = JwtUtil.extractUsername(refreshToken);

            // 2. Database se user ki latest details (role, id) fetch karein
            User user = userRepository.findByUsername(username);

            if (user == null) {
                JsonResponse.unauthorized(resp, "User associated with this session not found. Please log in again.");
                return;
            }

            // 3. Naye access token ke liye claims tayyar karein
            Map<String, Object> claims = new HashMap<>();// Aapke User model ke mutabiq
            claims.put("uid", user.getId());     // Aapke User model ke mutabiq

            // 4. Sirf naya ACCESS TOKEN generate karein
            String newAccessToken = JwtUtil.generateAccessToken(username, claims);

            // 5. Naya access token client ko wapas bhejein
            Map<String, String> responseData = new HashMap<>();
            responseData.put("accessToken", newAccessToken);

            JsonResponse.ok(resp, responseData);

        } catch (Exception e) {
            // Agar token se username nikalne mein ya DB access mein masla ho
            JsonResponse.serverError(resp, "An internal error occurred while refreshing the session.");
        }
    }

    private String extractRefreshToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
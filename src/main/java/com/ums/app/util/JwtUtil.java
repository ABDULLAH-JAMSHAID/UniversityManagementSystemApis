package com.ums.app.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.io.InputStream;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class JwtUtil {

    private static final Key KEY;
    private static final long ACCESS_TOKEN_EXPIRATION;
    private static final long REFRESH_TOKEN_EXPIRATION;

    static {
        try (InputStream input = JwtUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(input);

            String secretFromProps = props.getProperty("jwt.secret");
            String envSecret = System.getenv("JWT_SECRET"); // Env var overrides properties
            String SECRET = (envSecret != null && !envSecret.isBlank()) ? envSecret : secretFromProps;

            ACCESS_TOKEN_EXPIRATION = Long.parseLong(props.getProperty("jwt.accessToken.expirationMillis"));
            REFRESH_TOKEN_EXPIRATION = Long.parseLong(props.getProperty("jwt.refreshToken.expirationMillis"));

            KEY = Keys.hmacShaKeyFor(SECRET.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to initialize JwtUtil: " + e.getMessage());
        }
    }

    public static String generateAccessToken(String subject, Map<String,Object> claims) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims) // Roles + Id ko claim mein add karen
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static String generateRefreshToken(String subject) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token).getBody();
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    public static Jws<Claims> parseToken(String token) throws JwtException {

        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);

    }
}
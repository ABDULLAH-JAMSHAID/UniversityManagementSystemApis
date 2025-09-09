package com.ums.app.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Properties;


public class JwtUtil {

    private static final String SECRET;
    private static final long EXPIRATION;
    private static final Key KEY;

    static {

        try {

            Properties props = new Properties();
            props.load(JwtUtil.class.getClassLoader().getResourceAsStream("application.properties"));
            String secretFromProps = props.getProperty("jwt.secret");
            String env = System.getenv("JWT_SECRET"); // env var overrides properties
            SECRET = (env != null && !env.isBlank()) ? env : secretFromProps;
            EXPIRATION = Long.parseLong(props.getProperty("jwt.expirationMillis", "3600000"));
            KEY = Keys.hmacShaKeyFor(SECRET.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        } catch (Exception e) {

            throw new ExceptionInInitializerError(e);

        }

    }

    public static String generateToken(String subject, Map<String, Object> claims) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION))
                .signWith(KEY)
                .compact();

    }

    public static Jws<Claims> parseToken(String token) throws JwtException {

        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);

    }

}

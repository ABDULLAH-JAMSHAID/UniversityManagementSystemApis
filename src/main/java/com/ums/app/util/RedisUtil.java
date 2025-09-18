package com.ums.app.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.Properties;

public class RedisUtil {
    private static final JedisPool pool;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    static {
        try (InputStream input = RedisUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(input);
            String host = props.getProperty("redis.host", "localhost");
            int port = Integer.parseInt(props.getProperty("redis.port", "6379"));

            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(10); // Configure pool size as needed
            pool = new JedisPool(poolConfig, host, port);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to initialize RedisUtil: " + e.getMessage());
        }
    }

    public static void blacklistToken(String token, long expirySeconds) {
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(BLACKLIST_PREFIX + token, expirySeconds, "true");
        }
    }

    public static boolean isTokenBlacklisted(String token) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(BLACKLIST_PREFIX + token);
        }
    }

    public static void closePool() {
        if (pool != null) {
            pool.close();
        }
    }
}
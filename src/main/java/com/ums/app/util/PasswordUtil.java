package com.ums.app.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    public static String hash(String password) {

        return BCrypt.withDefaults().hashToString(12, password.toCharArray());

    }

    public static boolean verify(String password, String hashed) {

        BCrypt.Result res = BCrypt.verifyer().verify(password.toCharArray(), hashed);
        return res.verified;

    }
}

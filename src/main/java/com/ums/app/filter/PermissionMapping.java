package com.ums.app.filter;

import com.ums.app.model.Permission;

import java.util.Map;

public class PermissionMapping {
    private static final Map<String, Permission> PERMISSION_MAP = Map.ofEntries(
            // Course related
            Map.entry("POST:/api/addCourse", Permission.ADD_NEW_COURSE),
            Map.entry("PUT:/api/updateCourse/*", Permission.UPDATE_COURSE),
            Map.entry("GET:/api/getAllCourses", Permission.GET_ALL_COURSES),

            // User related
            Map.entry("DELETE:/api/deleteUser/*", Permission.DELETE_USER),
            Map.entry("GET:/api/getAllUsers", Permission.GET_ALL_USERS),
            Map.entry("PUT:/api/updateUserProfile/*", Permission.UPDATE_USER_PROFILE),
            Map.entry("GET:/api/viewUserProfile/*", Permission.VIEW_USER_PROFILE)
    );


    public static Permission getRequiredPermission(String method, String path) {

        if (path.contains("/api/")) {
            path = path.substring(path.indexOf("/api/"));
        }

        String key = method + ":" + path;
        if (PERMISSION_MAP.containsKey(key)) {
            return PERMISSION_MAP.get(key);
        }

        // Check wildcard entries
        for (var entry : PERMISSION_MAP.entrySet()) {
            String mapKey = entry.getKey();
            if (mapKey.endsWith("/*")) {
                String base = mapKey.substring(0, mapKey.length() - 2); // remove /*
                if (key.startsWith(base)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }


}

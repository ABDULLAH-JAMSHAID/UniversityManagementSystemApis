package com.ums.app.util;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonResponse {
    private static final Gson gson = new Gson();

    private static void write(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(body));
    }

    //  Success response (200 OK)
    public static void ok(HttpServletResponse resp, Object body) throws IOException {
        write(resp, HttpServletResponse.SC_OK, body);
    }
    //  Generic error response with custom status and message
    public static void error(HttpServletResponse resp, int status, String message) throws IOException {
        write(resp, status, Map.of("error", message));
    }


    //  Created response (201 Created)
    public static void created(HttpServletResponse resp, Object body) throws IOException {
        write(resp, HttpServletResponse.SC_CREATED, body);
    }

    //  Bad Request (400)
    public static void badRequest(HttpServletResponse resp, String message) throws IOException {
        write(resp, HttpServletResponse.SC_BAD_REQUEST, Map.of("error", message));
    }
    //  conflict Request (409)
    public static void conflict(HttpServletResponse resp, String message) throws IOException {
        write(resp, HttpServletResponse.SC_CONFLICT, Map.of("error", message));
    }

    //  Unauthorized (401)
    public static void unauthorized(HttpServletResponse resp, String message) throws IOException {
        write(resp, HttpServletResponse.SC_UNAUTHORIZED, Map.of("error", message));
    }

    //  Forbidden (403)
    public static void forbidden(HttpServletResponse resp, String message) throws IOException {
        write(resp, HttpServletResponse.SC_FORBIDDEN, Map.of("error", message));
    }

    //  Not Found (404)
    public static void notFound(HttpServletResponse resp, String message) throws IOException {
        write(resp, HttpServletResponse.SC_NOT_FOUND, Map.of("error", message));
    }

    //  Internal Server Error (500)
    public static void serverError(HttpServletResponse resp, String message) throws IOException {
        write(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Map.of("error", message));
    }
}

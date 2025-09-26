package com.ums.app.util;

public class sql {

    public static final String registerUser ="insert into ums.users (username,full_name,email,hashed_password)\n" +
            "VALUES (?,?,?,?) RETURNING id ;";

    public static final String insertStudent = "insert into ums.students (user_id) VALUES (?);";

    public static final String assignRole="insert into ums.user_roles VALUES(?,?)";

    public static final String insertTeacher = "insert into ums.teachers (user_id) VALUES (?);";

    public static final String findByUsername = "select id,username,full_name,email,hashed_password\n" +
            "from ums.users where username=?";

    public static final String getUserRoles ="SELECT g.name\n" +
            "FROM ums.groups g\n" +
            "JOIN ums.user_groups ug ON g.id = ug.group_id\n" +
            "WHERE ug.user_id = ?;";

    public static final String findUserById = "select id,username,full_name,email \n" +
            "from ums.users where id=?";

    public static final String updateUserProfile = "update ums.users set username=? ,full_name=? , email=? where id=?;";

    public static final String deleteUser="delete from ums.users where id=?";

    public static final String getAllUsers="select * from ums.users where role=?";

    public static final String addNewCourse="insert into ums.courses (code,title,credit_hours ) VALUES (?,?,?)";

    public static final String checkCourseByCode="select * from ums.courses where code=?";

    public static final String checkCourseById="select * from ums.courses where id=?";

    public static final String getAllCourses="select * from ums.courses";

    public static final String updateCourse="update ums.courses set code=? , title=? , credit_hours=? where id=?";

    public static final String userHasPermission="SELECT DISTINCT p.name AS permission_name \n" +
            "FROM ums.users u \n" +
            "JOIN ums.user_groups ug ON u.id = ug.user_id \n" +
            "JOIN ums.group_permissions gp ON ug.group_id = gp.group_id \n" +
            "JOIN ums.permissions p ON gp.permission_id = p.id \n" +
            "WHERE u.id=?";
}



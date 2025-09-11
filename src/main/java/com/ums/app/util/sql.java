package com.ums.app.util;

public class sql {

    public static final String registerUser = "insert into ums.users (username,full_name,email,hashed_password,role) \n" +
            "VALUES (?,?,?,?,?);";

    public static final String insertStudent = "insert into ums.students (user_id) VALUES (?);";

    public static final String insertTeacher = "insert into ums.teachers (user_id) VALUES (?);";

    public static final String findByUsername = "select id,username,full_name,email,hashed_password,role \n" +
            "from ums.users where username=?";

    public static final String findUserById = "select id,username,full_name,email,role \n" +
            "from ums.users where id=?";

    public static final String updateUserProfile = "update ums.users set username=? ,full_name=? , email=? where id=?;";

    public static final String deleteUser="delete from ums.users where id=?";


}



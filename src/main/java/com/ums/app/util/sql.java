package com.ums.app.util;

public class sql {

    public static final String registerUser="insert into ums.users (username,full_name,email,hashed_password,role) \n" +
            "VALUES (?,?,?,?,?);";

    public static final String insertStudent="insert into ums.students (user_id) VALUES (?);";

    public static final String insertTeacher="insert into ums.teachers (user_id) VALUES (?);";

    public static final String findByUsername="select id,username,full_name,email,hashed_password,role \n" +
            "from ums.users where username=?";
}

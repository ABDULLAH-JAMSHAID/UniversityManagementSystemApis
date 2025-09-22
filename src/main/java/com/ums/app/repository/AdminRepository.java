package com.ums.app.repository;

import com.ums.app.model.Courses;
import com.ums.app.model.User;
import com.ums.app.util.DBConnection;
import com.ums.app.util.sql;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {

    private final DataSource ds= DBConnection.getDataSource();

    public boolean deleteUser(int id) throws SQLException {

        try(Connection connection=ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.deleteUser)){

            preparedStatement.setInt(1,id);
            int rows=preparedStatement.executeUpdate();

            if (rows>0){
                return true;
            }


        }
        return false;
    }

    public List<User> getAllUsers(String role) throws SQLException {

        try(Connection connection=ds.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(sql.getAllUsers)){
            preparedStatement.setString(1,role);

            ResultSet rs=preparedStatement.executeQuery();

            List<User> users=new ArrayList<>();

            while (rs.next()){
                User user=new User();

                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFull_name(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
            return users;
        }

    }

    public boolean AddNewCourse(Courses courses) throws SQLException {

        try(Connection connection=ds.getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(sql.addNewCourse)){

            preparedStatement.setString(1,courses.getCode());
            preparedStatement.setString(2,courses.getTitle());
            preparedStatement.setDouble(3,courses.getCreditHours());

            int rows=preparedStatement.executeUpdate();

            if (rows>0){
                return true;
            }
        }
        return false;

    }

    public boolean checkCourseByCode(Courses courses) throws SQLException {

        try(Connection connection=ds.getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(sql.checkCourseByCode)){

            preparedStatement.setString(1,courses.getCode());
            ResultSet rs=preparedStatement.executeQuery();

            if (rs.next()){
                return true;
            }

        }
        return false;
    }

    public Courses checkCourseById(int id) throws SQLException {

        try(Connection connection=ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.checkCourseById)){

            preparedStatement.setInt(1,id);
            ResultSet rs=preparedStatement.executeQuery();

            if (rs.next()){
                Courses courses=new Courses();

                courses.setCode(rs.getString("code"));
                courses.setTitle(rs.getString("title"));
                courses.setCreditHours(rs.getDouble("credit_hours"));
                return courses;
            }

        }
        return null;
    }

    public List<Courses> getAllCourses() throws SQLException {

        try(Connection connection=ds.getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(sql.getAllCourses)){

            ResultSet rs=preparedStatement.executeQuery();

            List<Courses> courses=new ArrayList<>();

            while (rs.next()){
                Courses course=new Courses();
                course.setId(rs.getInt("id"));
                course.setCode(rs.getString("code"));
                course.setTitle(rs.getString("title"));
                course.setCreditHours(rs.getDouble("credit_hours"));
                courses.add(course);
            }
            return courses;

        }
    }

    public boolean updateCourse(Courses courses,int id) throws SQLException {
        try(Connection connection=ds.getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(sql.updateCourse)){

            preparedStatement.setString(1,courses.getCode());
            preparedStatement.setString(2,courses.getTitle());
            preparedStatement.setDouble(3,courses.getCreditHours());
            preparedStatement.setInt(4,id);

            int rows=preparedStatement.executeUpdate();

            if (rows>0){
                return true;
            }
        }
        return false;
    }
}

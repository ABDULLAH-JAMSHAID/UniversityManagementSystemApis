package com.ums.app.repository;

import com.ums.app.model.User;
import com.ums.app.model.UserRole;
import com.ums.app.util.DBConnection;
import com.ums.app.util.sql;

import javax.sql.DataSource;
import java.sql.*;

public class UserRepository {

    private final DataSource ds= DBConnection.getDataSource();

    public int saveUser(User user) throws SQLException {

        ResultSet generatedKeys=null;

        try(Connection connection=ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.registerUser, Statement.RETURN_GENERATED_KEYS);){

            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getFull_name());
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4,user.getPassword());
            preparedStatement.setString(5,user.getRole().name());

            int rows= preparedStatement.executeUpdate();
            if (rows<0){
                return -1;
            }

            generatedKeys= preparedStatement.getGeneratedKeys();

            if (user.getRole()== UserRole.student){

                if (generatedKeys.next()){
                    int userId = generatedKeys.getInt(1);
                    PreparedStatement preparedStatement1=connection.prepareStatement(sql.insertStudent);
                    preparedStatement1.setInt(1,userId);

                    int rows1=preparedStatement1.executeUpdate();
                    if (rows1>0){
                        preparedStatement1.close();
                        generatedKeys.close();
                        return userId;
                    }
                }

            }
            else{

                if (generatedKeys.next()){
                    int userId = generatedKeys.getInt(1);
                    PreparedStatement preparedStatement1=connection.prepareStatement(sql.insertTeacher);
                    preparedStatement1.setInt(1,userId);

                    int rows1=preparedStatement1.executeUpdate();
                    if (rows1>0){
                        preparedStatement1.close();
                        generatedKeys.close();
                        return userId;
                    }
                }

            }



        }
        return -1;

    }

    public User findByUsername(String username) throws SQLException {

        try(Connection connection=ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.findByUsername);){

            preparedStatement.setString(1,username);

            ResultSet rs=preparedStatement.executeQuery();


            if(rs.next()){
                User user=new User();


                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFull_name(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("hashed_password"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                return user;

            }

        }
        return null;

    }

    public User findById(int id) throws SQLException {

        try(Connection connection=ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.findUserById);){

            preparedStatement.setInt(1,id);

            ResultSet rs=preparedStatement.executeQuery();

            if(rs.next()){
                User user=new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFull_name(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(UserRole.valueOf(rs.getString("role")));
                return user;

            }

        }
        return null;

    }
}

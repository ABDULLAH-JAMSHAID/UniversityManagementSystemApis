package com.ums.app.repository;

import com.ums.app.model.Permission;
import com.ums.app.model.User;
import com.ums.app.util.DBConnection;
import com.ums.app.util.sql;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class UserRepository {

    private final DataSource ds= DBConnection.getDataSource();

    public int saveUser(User user) throws SQLException {

        try(Connection connection=ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.registerUser)){

           preparedStatement.setString(1,user.getUsername());
           preparedStatement.setString(2,user.getFull_name());
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4,user.getPassword());

            ResultSet rs=preparedStatement.executeQuery();

            if (rs.next()){
                return rs.getInt("id");
                }
            }
            return -1;
        }


        public boolean saveStudent(int userId) throws SQLException {
            try(Connection connection=ds.getConnection();
                PreparedStatement preparedStatement=connection.prepareStatement(sql.insertStudent)){

                preparedStatement.setInt(1,userId);

                int rows=preparedStatement.executeUpdate();
                if (rows>0){
                    return true;
                }
            }
            return false;
        }

        public boolean saveTeacher(int userId) throws SQLException {
        try(Connection connection=ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.insertTeacher)){

            preparedStatement.setInt(1,userId);

            int rows=preparedStatement.executeUpdate();
            if (rows>0){
                return true;
            }
        }
        return false;
    }

        public boolean assignRole(int userId,int roleId) throws SQLException {

            try(Connection connection =ds.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql.assignRole)){

                preparedStatement.setInt(1,userId);
                preparedStatement.setInt(2,roleId);

                int rows=preparedStatement.executeUpdate();
                if (rows>0){
                    return true;
                }

            }
            return false;
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
                return user;
            }

        }
        return null;

    }

        public List<String> getUserRoles(int userId) {

            try (Connection connection = ds.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql.getUserRoles)) {

                preparedStatement.setInt(1, userId);

                ResultSet rs = preparedStatement.executeQuery();

                List<String> roles = new java.util.ArrayList<>();

                while (rs.next()) {
                    roles.add(rs.getString("name"));
                }
                return roles;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public User findById(int id) {

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
                    user.setPassword(null);
                    return user;
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;

        }

    public boolean isRoleAllowedForPath(int roleId, String path) {
        String sql = "SELECT COUNT(*) FROM ums.api_permissions " +
                "WHERE role_id = ? AND ? LIKE path";

        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roleId);
            stmt.setString(2, path);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public Integer getRoleIdByUserId(Integer userId) {
        try (Connection conn = ds.getConnection()) {
            String sql = "SELECT role_id FROM ums.user_roles WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("role_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean userHasPermission(int userId, Permission requiredPermission) {
        Set<String> userPermissions = new HashSet<>();

        try ( Connection connection=ds.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql.userHasPermission)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userPermissions.add(rs.getString("permission_name").toUpperCase(Locale.ROOT));
            }
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            return false;
        }

        return userPermissions.contains(requiredPermission.name().toUpperCase());
    }




}

package com.ums.app.repository;

import com.ums.app.model.User;
import com.ums.app.util.DBConnection;
import com.ums.app.util.sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentRepository {

    private final DataSource ds=DBConnection.getDataSource();

    public boolean updateUserProfile(int id,User user) throws SQLException {

        try (Connection connection=ds.getConnection();
             PreparedStatement preparedStatement=connection.prepareStatement(sql.updateUserProfile)){

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2,user.getFull_name());
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setInt(4,id);

            int rows=preparedStatement.executeUpdate();
            if (rows>0){
                return true;
            }
        }
        return false;

    }
}

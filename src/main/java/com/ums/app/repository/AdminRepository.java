package com.ums.app.repository;

import com.ums.app.util.DBConnection;
import com.ums.app.util.sql;
import com.zaxxer.hikari.util.ConcurrentBag;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Predicate;

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
}

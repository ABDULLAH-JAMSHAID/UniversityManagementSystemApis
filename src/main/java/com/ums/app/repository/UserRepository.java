package com.ums.app.repository;

import com.ums.app.model.User;
import com.ums.app.util.DBConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Predicate;

public class UserRepository {

    private final DataSource ds= DBConnection.getDataSource();

    public long saveUser(User user) throws SQLException {


       return 1;


    }
}

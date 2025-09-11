package com.ums.app.service;

import com.ums.app.model.User;
import com.ums.app.repository.AdminRepository;
import com.ums.app.repository.UserRepository;

import java.sql.SQLException;

public class AdminService {

    private final AdminRepository adminRepository=new AdminRepository();
    private final UserRepository userRepository=new UserRepository();

    public boolean deleteUser(int id) throws SQLException {

        User user =userRepository.findById(id);
        if (user==null){
            return false;
        }
        return adminRepository.deleteUser(id);



    }
}

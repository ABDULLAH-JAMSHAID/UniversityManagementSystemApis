package com.ums.app.service;

import com.ums.app.model.User;
import com.ums.app.repository.StudentRepository;
import com.ums.app.repository.UserRepository;

import java.sql.SQLException;

public class StudentService {

    private final UserRepository userRepository = new UserRepository();
    private final StudentRepository studentRepository = new StudentRepository();

    public User viewUserProfile(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return null; // business level response (user not found)
        }
        return user;
    }

    public User updateUserProfile(int id, String username, String fullname, String email) {
        try {
            User user = userRepository.findById(id);
            if (user == null) {
                return null; // not found
            }

            // update only non-empty fields
            if (fullname != null && !fullname.trim().isEmpty()) {
                user.setFull_name(fullname);
            }
            if (email != null && !email.trim().isEmpty()) {
                user.setEmail(email);
            }
            if (username != null && !username.trim().isEmpty()) {
                user.setUsername(username);
            }

            boolean ok = studentRepository.updateUserProfile(id, user);
            if (!ok) {
                return null; // update failed
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating user profile (id=" + id + ")", e);
        }
    }
}

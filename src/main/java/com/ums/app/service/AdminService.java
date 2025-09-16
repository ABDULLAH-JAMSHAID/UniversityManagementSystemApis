package com.ums.app.service;

import com.ums.app.model.Courses;
import com.ums.app.model.User;
import com.ums.app.repository.AdminRepository;
import com.ums.app.repository.UserRepository;
import java.sql.SQLException;
import java.util.List;

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

    public List<User> getAllUsers(String role) throws SQLException {

        return adminRepository.getAllUsers(role);
    }

    public boolean addNewCourse(Courses courses) throws SQLException {


        boolean ok=adminRepository.checkCourseByCode(courses);
        if (ok){
            throw new SQLException();
        }

       return adminRepository.AddNewCourse(courses);
    }

    public List<Courses> getAllCourses() throws SQLException {

        return adminRepository.getAllCourses();
    }

    public boolean updateCourse(int id,String code,String title,Double credit_hours) throws SQLException {

        Courses courses=adminRepository.checkCourseById(id);
        if (courses==null){
            return false;
        }
        if (!(code==null || code.trim().isEmpty() )){
            courses.setCode(code);
        }
        if (!(title==null || title.trim().isEmpty())){
            courses.setTitle(title);
        }
        if (!(credit_hours==null || credit_hours<=0 || credit_hours >3 ) ){
            courses.setCreditHours(credit_hours);
        }

        return adminRepository.updateCourse(courses,id);



    }
}

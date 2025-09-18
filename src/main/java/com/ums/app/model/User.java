package com.ums.app.model;
import java.sql.Date;
import java.sql.Timestamp;


public class User {

    private int tokenVersion;

    private int id;

    private String username;

    private String full_name;

    private String email;

    private String password;

    private UserRole role;

    private Timestamp registeredAt;

    public User() {
    }

    public User(String username, String full_name, String email, String password, UserRole role) {
        this.username = username;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User( int id,String username, String full_name, String email, UserRole role, Timestamp registeredAt) {
        this.id = id;
        this.username = username;
        this.full_name = full_name;
        this.email = email;
        this.role = role;
        this.registeredAt=registeredAt;
    }
    public int getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(int tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

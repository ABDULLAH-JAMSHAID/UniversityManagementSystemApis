package com.ums.app.model;

public class User {

    private int id;
    private String username;
    private String full_name;
    private String email;
    private String password;

    public User (){

    }


    public User(int id, String username, String full_name, String email, String password) {
        this.id = id;
        this.username = username;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

    public User(String username, String full_name, String email, String password) {
        this.username = username;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

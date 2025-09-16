package com.ums.app.model;

public class Courses {

    private int id;

    private String code;

    private String title;

    private Double creditHours;

    public Courses() {
    }

    public Courses(String code, String title, Double creditHours) {
        this.code = code;
        this.title = title;
        this.creditHours = creditHours;
    }

    public Courses(int id, String code, String title, Double creditHours) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.creditHours = creditHours;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(Double creditHours) {
        this.creditHours = creditHours;
    }
}

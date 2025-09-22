package com.ums.app.model;

import java.util.List;

public class Role {

    private int id;
    private List<String> name;

    public Role() {
    }
    public Role(int id, List<String> name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}

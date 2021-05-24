package com.mobdeve.cait.mp;

import java.io.Serializable;

public class genreClass implements Serializable {
    int id;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public genreClass(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

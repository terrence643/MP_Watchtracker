package com.mobdeve.cait.mp;

public class MovieClass {

    String id;
    String name;
    String img;
    String language;
    String overview;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MovieClass(String id, String name, String img, String language, String overview) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.language = language;
        this.overview = overview;
    }

    public MovieClass() {
    }
}
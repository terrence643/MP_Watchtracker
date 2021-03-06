package com.mobdeve.cait.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class TMDBClass implements Parcelable {

    String id;
    String name;
    String img;
    String language;
    String overview;
    String airdate ;
    String type ;

    protected TMDBClass(Parcel in) {
        id = in.readString();
        name = in.readString();
        img = in.readString();
        language = in.readString();
        overview = in.readString();
        airdate = in.readString();
        type = in.readString();

    }

    public static final Creator<TMDBClass> CREATOR = new Creator<TMDBClass>() {
        @Override
        public TMDBClass createFromParcel(Parcel in) {
            return new TMDBClass(in);
        }

        @Override
        public TMDBClass[] newArray(int size) {
            return new TMDBClass[size];
        }
    };

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

    public String getAirdate() {
        return airdate;
    }

    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    public TMDBClass(String id, String name, String img, String language, String overview, String airdate, String type) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.language = language;
        this.overview = overview;
        this.airdate = airdate ;
        this.type = type ;
    }

    public TMDBClass() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(img);
        dest.writeString(language);
        dest.writeString(overview);
        dest.writeString(airdate);
        dest.writeString(type);

    }
}

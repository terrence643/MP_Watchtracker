package com.mobdeve.cait.mp;

import android.os.Parcel;
import android.os.Parcelable;

public class TvClass implements Parcelable {

    String id;
    String name;
    String img;
    String language;
    String overview;


    protected TvClass(Parcel in) {
        id = in.readString();
        name = in.readString();
        img = in.readString();
        language = in.readString();
        overview = in.readString();
    }

    public static final Creator<TvClass> CREATOR = new Creator<TvClass>() {
        @Override
        public TvClass createFromParcel(Parcel in) {
            return new TvClass(in);
        }

        @Override
        public TvClass[] newArray(int size) {
            return new TvClass[size];
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

    public TvClass(String id, String name, String img, String language, String overview) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.language = language;
        this.overview = overview;
    }

    public TvClass() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(img);
        dest.writeString(language);
        dest.writeString(overview);
    }
}

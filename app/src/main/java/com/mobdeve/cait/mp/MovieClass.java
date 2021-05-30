package com.mobdeve.cait.mp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

public class MovieClass implements Parcelable {

    String id;
    String name;
    String img;
    String language;
    String overview;

    protected MovieClass(Parcel in) {
        id = in.readString();
        name = in.readString();
        img = in.readString();
        language = in.readString();
        overview = in.readString();
    }

    public static final Creator<MovieClass> CREATOR = new Creator<MovieClass>() {
        @Override
        public MovieClass createFromParcel(Parcel in) {
            return new MovieClass(in);
        }

        @Override
        public MovieClass[] newArray(int size) {
            return new MovieClass[size];
        }
    };

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

    public class showMovie{

    }
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

    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
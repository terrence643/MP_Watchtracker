package com.mobdeve.cait.mp;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SeasonClass implements Serializable, Parcelable {

    String air_date;
    int episode_count;
    int id;
    String name;
    String overview;
    String poster_path;
    int season_number;

    protected SeasonClass(Parcel in) {
        air_date = in.readString();
        episode_count = in.readInt();
        id = in.readInt();
        name = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        season_number = in.readInt();
    }

    public static final Creator<SeasonClass> CREATOR = new Creator<SeasonClass>() {
        @Override
        public SeasonClass createFromParcel(Parcel in) {
            return new SeasonClass(in);
        }

        @Override
        public SeasonClass[] newArray(int size) {
            return new SeasonClass[size];
        }
    };

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public void setEpisode_count(int episode_count) {
        this.episode_count = episode_count;
    }

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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public SeasonClass(String air_date, int episode_count, int id, String name, String overview, String poster_path, int season_number) {
        this.air_date = air_date;
        this.episode_count = episode_count;
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.poster_path = poster_path;
        this.season_number = season_number;
    }

    public SeasonClass() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(air_date);
        dest.writeInt(episode_count);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeInt(season_number);
    }
}

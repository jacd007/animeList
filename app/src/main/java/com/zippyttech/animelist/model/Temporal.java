package com.zippyttech.animelist.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zippyttech on 07/09/17.
 */

public class Temporal implements Serializable,Parcelable {

    private int id;
    private String title;
    private String comments;
    private int Image;


    public Temporal(int id, String title, String comments) {
        this.id = id;
        this.title = title;
        this.comments = comments;
    }


    public Temporal(int id, String title, int Image) {
        this.id = id;
        this.title = title;
       this.Image=Image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Temporal(Parcel in) {
    }

    public static final Creator<Temporal> CREATOR = new Creator<Temporal>() {
        @Override
        public Temporal createFromParcel(Parcel in) {
            return new Temporal(in);
        }

        @Override
        public Temporal[] newArray(int size) {
            return new Temporal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Temporal temporal = (Temporal) o;

        if (id != temporal.id) return false;
        if (title != null ? !title.equals(temporal.title) : temporal.title != null) return false;
        return comments != null ? comments.equals(temporal.comments) : temporal.comments == null;

    }
}

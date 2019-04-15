package com.zippyttech.animelist.model;

import android.graphics.Bitmap;

/**
 * Created by zippyttech on 27/10/18.
 */

public class Animes {
    private int id, idAnime, capitule;
    private String name, subName, status, color;
    private String dateCreated, dateUpdate, image;
    private boolean deleted, enabled;
    private Bitmap btpImage;

    public Animes(){
    }
    public Animes(int id, int idAnime, int capitule, String name, String subName,
                  String status, String dateCreated, String dateUpdate, String color, String image) {
        this.id = id;
        this.idAnime = idAnime;
        this.name = name;
        this.subName = subName;
        this.dateCreated = dateCreated;
        this.dateUpdate = dateUpdate;
        this.status = status;
        this.capitule = capitule;
        this.color = color;
        this.image = image;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(int idAnime) {
        this.idAnime = idAnime;
    }

    public int getCapitule() {
        return capitule;
    }

    public void setCapitule(int capitule) {
        this.capitule = capitule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Bitmap getBtpImage() {
        return btpImage;
    }

    public void setBtpImage(Bitmap btpImage) {
        this.btpImage = btpImage;
    }
}

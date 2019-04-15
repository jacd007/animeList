package com.zippyttech.animelist.model;

/**
 * Created by zippyttech on 15/12/18.
 */

public class Pendings {
    private int id, IdProduct;
    private String Query, Type, Status;

    public Pendings() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduct() {
        return IdProduct;
    }

    public void setIdProduct(int IdProduct) {
        IdProduct = IdProduct;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

package com.rogergcc.micrudmoongose.model;

/**
 * Created by rogergcc on 11/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */

public class Productos {
    private String name;
    private double price;
    private String _id;
    public Productos(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public Productos(String name, double price, String _id) {
        this.name = name;
        this.price = price;
        this._id=_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return _id;
    }
    public void setId(String _id) {
        this._id = _id;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
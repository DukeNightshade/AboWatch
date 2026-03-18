package com.dukenightshade.abowatch.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subscriptions")
public class Subscription {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double price;
    private String category;
    private String startDate;
    private int noticePeriod;

    public Subscription(String name, double price, String category, String startDate, int noticePeriod) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.startDate = startDate;
        this.noticePeriod = noticePeriod;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getNoticePeriod() {
        return noticePeriod;
    }

    public void setNoticePeriod(int noticePeriod) {
        this.noticePeriod = noticePeriod;
    }
}

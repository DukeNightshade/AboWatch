package com.dukenightshade.abowatch.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entität für ein Abonnement in der Room-Datenbank.
 * @author Nico Hoffmann
 * @version 1.0
 */
@Entity(tableName = "subscriptions")
public class Subscription {

    // ====================================
    // Constants
    // ====================================

    public static final String BILLING_CYCLE_MONTHLY = "monthly";
    public static final String BILLING_CYCLE_YEARLY  = "yearly";

    // ====================================
    // Database Fields
    // ====================================

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double price;
    private String category;
    private String startDate;
    private int noticePeriod;
    private String billingCycle;
    private boolean isCancelled = false;
    // ====================================
    // Constructor
    // ====================================

    public Subscription(String name, double price, String category,
                        String startDate, int noticePeriod, String billingCycle) {
        this.name         = name;
        this.price        = price;
        this.category     = category;
        this.startDate    = startDate;
        this.noticePeriod = noticePeriod;
        this.billingCycle = billingCycle;
    }

    // ====================================
    // Getter & Setter
    // ====================================

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

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}

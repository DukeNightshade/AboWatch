package com.dukenightshade.abowatch.ui.adapter;

public class HeaderItem implements ListItem {

    private final String categoryName;
    private final double categoryTotal;

    public HeaderItem(String categoryName, double categoryTotal) {
        this.categoryName = categoryName;
        this.categoryTotal = categoryTotal;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getCategoryTotal() {
        return categoryTotal;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}

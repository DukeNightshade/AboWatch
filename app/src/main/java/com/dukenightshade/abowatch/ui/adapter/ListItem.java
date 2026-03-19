package com.dukenightshade.abowatch.ui.adapter;

public interface ListItem {
    int TYPE_HEADER = 0;
    int TYPE_SUBSCRIPTION = 1;

    int getType();
}

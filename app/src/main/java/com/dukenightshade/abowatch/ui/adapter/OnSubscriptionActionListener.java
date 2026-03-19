package com.dukenightshade.abowatch.ui.adapter;

import com.dukenightshade.abowatch.model.Subscription;

public interface OnSubscriptionActionListener {
    void onEdit(Subscription subscription);
    void onDelete(Subscription subscription);
}

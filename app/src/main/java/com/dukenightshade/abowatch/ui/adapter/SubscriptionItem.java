package com.dukenightshade.abowatch.ui.adapter;

import com.dukenightshade.abowatch.model.Subscription;

public class SubscriptionItem implements ListItem {

    private final Subscription subscription;

    public SubscriptionItem(Subscription subscription) {
        this.subscription = subscription;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public int getType() {
        return TYPE_SUBSCRIPTION;
    }
}

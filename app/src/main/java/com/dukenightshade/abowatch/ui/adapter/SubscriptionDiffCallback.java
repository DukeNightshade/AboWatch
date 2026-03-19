package com.dukenightshade.abowatch.ui.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.dukenightshade.abowatch.model.Subscription;

import java.util.List;

/**
 * DiffUtil Callback für die Abo-Liste.
 * Berechnet Unterschiede zwischen zwei Listen effizient.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class SubscriptionDiffCallback extends DiffUtil.Callback {

    // ====================================
    // Instance Variables
    // ====================================

    private final List<ListItem> oldList;
    private final List<ListItem> newList;

    // ====================================
    // Constructors
    // ====================================

    public SubscriptionDiffCallback(List<ListItem> oldList, List<ListItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldPos, int newPos) {
        ListItem oldItem = oldList.get(oldPos);
        ListItem newItem = newList.get(newPos);

        if (oldItem.getType() != newItem.getType()) return false;

        if (oldItem instanceof SubscriptionItem && newItem instanceof SubscriptionItem) {
            return ((SubscriptionItem) oldItem).getSubscription().getId() ==
                    ((SubscriptionItem) newItem).getSubscription().getId();
        }
        if (oldItem instanceof HeaderItem && newItem instanceof HeaderItem) {
            return ((HeaderItem) oldItem).getCategoryName()
                    .equals(((HeaderItem) newItem).getCategoryName());
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldPos, int newPos) {
        ListItem oldItem = oldList.get(oldPos);
        ListItem newItem = newList.get(newPos);

        if (oldItem instanceof SubscriptionItem && newItem instanceof SubscriptionItem) {
            Subscription o = ((SubscriptionItem) oldItem).getSubscription();
            Subscription n = ((SubscriptionItem) newItem).getSubscription();
            return o.getName().equals(n.getName()) &&
                    o.getPrice() == n.getPrice() &&
                    o.getCategory().equals(n.getCategory());
        }
        if (oldItem instanceof HeaderItem && newItem instanceof HeaderItem) {
            return ((HeaderItem) oldItem).getCategoryTotal() ==
                    ((HeaderItem) newItem).getCategoryTotal();
        }
        return false;
    }
}

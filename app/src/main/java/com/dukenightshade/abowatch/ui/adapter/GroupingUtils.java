package com.dukenightshade.abowatch.ui.adapter;

import com.dukenightshade.abowatch.model.Subscription;
import java.util.ArrayList;
import java.util.List;

/**
 * Hilfsmethoden zur Gruppierung von Abos nach Kategorie.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class GroupingUtils {

    // ====================================
    // Constructors
    // ====================================

    private GroupingUtils() {
        // Utility-Class – No instancing allowed
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    public static List<ListItem> groupByCategory(List<Subscription> subscriptions) {
        List<ListItem> result = new ArrayList<>();
        String lastCategory = null;
        double categorySum = 0;
        List<Subscription> currentGroup = new ArrayList<>();

        for (Subscription s : subscriptions) {
            if (!s.getCategory().equals(lastCategory)) {
                if (lastCategory != null) {
                    result.add(new HeaderItem(lastCategory, categorySum));
                    for (Subscription sub : currentGroup) {
                        result.add(new SubscriptionItem(sub));
                    }
                }
                lastCategory = s.getCategory();
                categorySum = 0;
                currentGroup.clear();
            }
            categorySum += s.getPrice();
            currentGroup.add(s);
        }

        if (lastCategory != null) {
            result.add(new HeaderItem(lastCategory, categorySum));
            for (Subscription sub : currentGroup) {
                result.add(new SubscriptionItem(sub));
            }
        }

        return result;
    }
}

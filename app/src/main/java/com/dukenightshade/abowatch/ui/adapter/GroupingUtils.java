package com.dukenightshade.abowatch.ui.adapter;

import com.dukenightshade.abowatch.model.Subscription;
import java.util.ArrayList;
import java.util.List;

public class GroupingUtils {

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

        // Letzte Gruppe nicht vergessen
        if (lastCategory != null) {
            result.add(new HeaderItem(lastCategory, categorySum));
            for (Subscription sub : currentGroup) {
                result.add(new SubscriptionItem(sub));
            }
        }

        return result;
    }
}

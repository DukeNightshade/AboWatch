package com.dukenightshade.abowatch.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dukenightshade.abowatch.model.Subscription;
import java.util.List;

@Dao
public interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions ORDER BY name ASC")
    List<Subscription> getAllSubscriptions();

    @Insert
    void insertSubscription(Subscription subscription);

    @Update
    void updateSubscription(Subscription subscription);

    @Delete
    void deleteSubscription(Subscription subscription);

    @Query("SELECT SUM(price) FROM subscriptions")
    double getTotalMonthlyCosts();
}

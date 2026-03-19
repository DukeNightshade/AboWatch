package com.dukenightshade.abowatch.data;

import androidx.lifecycle.LiveData;
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
    LiveData<List<Subscription>> getAllSubscriptions();

    @Insert
    void insertSubscription(Subscription subscription);

    @Update
    void updateSubscription(Subscription subscription);

    @Delete
    void deleteSubscription(Subscription subscription);

    @Query("SELECT SUM(price) FROM subscriptions")
    LiveData<Double> getTotalMonthlyCosts();

    // NEU: für EditSubscriptionActivity
    @Query("SELECT * FROM subscriptions WHERE id = :id LIMIT 1")
    Subscription getById(int id);

    @Query("DELETE FROM subscriptions WHERE id = :id")
    void deleteById(int id);
}

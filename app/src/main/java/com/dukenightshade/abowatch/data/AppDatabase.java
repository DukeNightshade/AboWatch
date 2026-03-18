package com.dukenightshade.abowatch.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dukenightshade.abowatch.model.Subscription;

@Database(entities = {Subscription.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SubscriptionDao subscriptionDao();

    private static class Holder {
        private static AppDatabase instance;
    }

    public static AppDatabase getDatabase(final Context context) {
        if (Holder.instance == null) {
            Holder.instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "abowatch_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return Holder.instance;
    }
}
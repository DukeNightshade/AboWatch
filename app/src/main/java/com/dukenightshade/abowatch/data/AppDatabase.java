package com.dukenightshade.abowatch.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.dukenightshade.abowatch.model.Subscription;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Room-Datenbankklasse für AboWatch.
 * Verwaltet die lokale SQLite-Datenbank und stellt den DAO bereit.
 * @author Nico Hoffmann
 * @version 2.0
 */
@Database(entities = {Subscription.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // ====================================
    // Abstract DAO Methods
    // ====================================

    public abstract SubscriptionDao subscriptionDao();

    // ====================================
    // Instance Variables
    // ====================================

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    private static AppDatabase instance;

    // ====================================
    // Migrations
    // ====================================

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE subscriptions ADD COLUMN billingCycle TEXT NOT NULL DEFAULT 'monthly'"
            );
        }
    };

    // ====================================
    // Singleton
    // ====================================

    public static synchronized AppDatabase getDatabase(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "abowatch_db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }
}

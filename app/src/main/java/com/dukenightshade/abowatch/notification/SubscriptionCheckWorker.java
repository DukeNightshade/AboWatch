package com.dukenightshade.abowatch.notification;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.dukenightshade.abowatch.data.AppDatabase;
import com.dukenightshade.abowatch.model.Subscription;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * WorkManager Worker – prüft täglich alle Abos auf ablaufende Kündigungsfristen.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class SubscriptionCheckWorker extends Worker {

    // ====================================
    // Constants
    // ====================================

    private static final int NOTIFICATION_DAYS_BEFORE = 7;
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String TAG = "SubscriptionCheckWorker";

    // ====================================
    // Constructor
    // ====================================

    public SubscriptionCheckWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    @NonNull
    @Override
    public Result doWork() {
        List<Subscription> subscriptions = AppDatabase
                .getDatabase(getApplicationContext())
                .subscriptionDao()
                .getAllSync();

        for (Subscription sub : subscriptions) {
            checkAndNotify(sub);
        }
        return Result.success();
    }

    private void checkAndNotify(Subscription sub) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
            Date startDate = sdf.parse(sub.getStartDate());
            if (startDate == null) return;

            Date nextCancellationDate = getNextCancellationDate(startDate, sub.getBillingCycle());
            Date notifyDate = getDaysBeforeDate(nextCancellationDate);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            if (today.getTime().equals(notifyDate)) {
                String cancellationDate = sdf.format(nextCancellationDate);
                NotificationHelper.sendNotification(
                        getApplicationContext(),
                        sub.getId(),
                        sub.getName(),
                        cancellationDate
                );
            }
        } catch (ParseException e) {
            Log.e(TAG, "Failed to parse date for subscription: " + sub.getName(), e);
        }
    }

    // ====================================
    // Utility Methods
    // ====================================

    private Date getNextCancellationDate(Date startDate, String billingCycle) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        Calendar today = Calendar.getInstance();

        while (cal.before(today)) {
            if ("yearly".equals(billingCycle)) {
                cal.add(Calendar.YEAR, 1);
            } else {
                cal.add(Calendar.MONTH, 1);
            }
        }
        return cal.getTime();
    }

    private Date getDaysBeforeDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -NOTIFICATION_DAYS_BEFORE);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}

package com.dukenightshade.abowatch.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.dukenightshade.abowatch.data.AppDatabase;
import com.dukenightshade.abowatch.data.SubscriptionDao;
import com.dukenightshade.abowatch.model.Subscription;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ViewModel für die Abo-Liste.
 * Vermittelt zwischen UI und Datenbank-DAO.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class SubscriptionViewModel extends AndroidViewModel {

    // ====================================
    // Constants
    // ====================================

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    // ====================================
    // Instance Variables
    // ====================================

    private final SubscriptionDao dao;
    public final LiveData<List<Subscription>> allSubscriptions;
    public final LiveData<Double> totalCosts;

    // ====================================
    // Constructor
    // ====================================

    public SubscriptionViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getDatabase(application).subscriptionDao();
        allSubscriptions = dao.getAllSubscriptions();
        totalCosts = dao.getTotalMonthlyCosts();
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    public void insert(Subscription subscription) {
        AppDatabase.databaseWriteExecutor.execute(
                () -> dao.insertSubscription(subscription)
        );
    }

    public void update(Subscription subscription) {
        AppDatabase.databaseWriteExecutor.execute(
                () -> dao.updateSubscription(subscription)
        );
    }

    public void delete(Subscription subscription) {
        AppDatabase.databaseWriteExecutor.execute(
                () -> dao.deleteSubscription(subscription)
        );
    }

    public LiveData<Subscription> getById(int id) {
        MutableLiveData<Subscription> result = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(
                () -> result.postValue(dao.getById(id))
        );
        return result;
    }

    public void deleteById(int id) {
        AppDatabase.databaseWriteExecutor.execute(
                () -> dao.deleteById(id)
        );
    }

    public void deleteExpiredCancelled() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Subscription> all = dao.getAllSync();
            for (Subscription sub : all) {
                if (sub.isCancelled() && isExpired(sub)) {
                    dao.deleteSubscription(sub);
                }
            }
        });
    }

    // ====================================
    // Utility Methods
    // ====================================

    private boolean isExpired(Subscription sub) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT);
            Date startDate = sdf.parse(sub.getStartDate());
            if (startDate == null) return false;

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            Calendar today = Calendar.getInstance();

            while (cal.before(today)) {
                if (Subscription.BILLING_CYCLE_YEARLY.equals(sub.getBillingCycle())) {
                    cal.add(Calendar.YEAR, 1);
                } else {
                    cal.add(Calendar.MONTH, 1);
                }
            }
            return cal.before(today);
        } catch (ParseException e) {
            return false;
        }
    }
}

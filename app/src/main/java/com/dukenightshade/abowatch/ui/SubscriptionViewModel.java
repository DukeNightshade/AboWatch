package com.dukenightshade.abowatch.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.dukenightshade.abowatch.data.AppDatabase;
import com.dukenightshade.abowatch.data.SubscriptionDao;
import com.dukenightshade.abowatch.model.Subscription;
import java.util.List;

public class SubscriptionViewModel extends AndroidViewModel {

    private final SubscriptionDao dao;
    public final LiveData<List<Subscription>> allSubscriptions;
    public final LiveData<Double> totalCosts;

    public SubscriptionViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getDatabase(application).subscriptionDao();
        allSubscriptions = dao.getAllSubscriptions();
        totalCosts = dao.getTotalMonthlyCosts();
    }

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
}

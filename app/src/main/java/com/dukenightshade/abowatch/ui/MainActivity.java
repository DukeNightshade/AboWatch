package com.dukenightshade.abowatch.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.model.Subscription;
import com.dukenightshade.abowatch.notification.NotificationHelper;
import com.dukenightshade.abowatch.notification.SubscriptionCheckWorker;
import com.dukenightshade.abowatch.ui.adapter.OnSubscriptionActionListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Haupt-Activity der AboWatch-App.
 * Koordiniert Dashboard, Abo-Liste und Navigation.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    // ====================================
    // Instance Variables
    // ====================================

    private SubscriptionAdapter adapter;
    private boolean editModeActive = false;

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> { }
            );

    // ====================================
    // Lifecycle Methods
    // ====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.activity.EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        applyWindowInsets();
        setupRecyclerView();
        setupFabs();
        observeViewModel();
        setupNotifications();
    }

    // ====================================
    // Setup Methods
    // ====================================

    private void applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupRecyclerView() {
        RecyclerView rvSubscriptions = findViewById(R.id.rvSubscriptions);
        adapter = new SubscriptionAdapter();
        rvSubscriptions.setLayoutManager(new LinearLayoutManager(this));
        rvSubscriptions.setAdapter(adapter);

        adapter.setOnSubscriptionActionListener(new OnSubscriptionActionListener() {
            @Override
            public void onEdit(Subscription subscription) {
                Intent intent = new Intent(MainActivity.this, EditSubscriptionActivity.class);
                intent.putExtra(EditSubscriptionActivity.EXTRA_SUBSCRIPTION_ID, subscription.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Subscription subscription) {
                showDeleteDialog(subscription);
            }
        });
    }

    private void setupFabs() {
        FloatingActionButton fabAdd = findViewById(R.id.fabAddSubscription);
        ExtendedFloatingActionButton fabEdit = findViewById(R.id.fabEditMode);

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddSubscriptionActivity.class))
        );

        fabEdit.setOnClickListener(v -> {
            editModeActive = !editModeActive;
            adapter.setEditMode(editModeActive);
            fabEdit.setText(editModeActive ?
                    getString(R.string.button_done) :
                    getString(R.string.button_edit));
        });
    }

    private void observeViewModel() {
        TextView tvTotalCosts = findViewById(R.id.tvTotalCosts);
        SubscriptionViewModel viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);

        viewModel.totalCosts.observe(this, total -> {
            if (total == null) total = 0.0;
            tvTotalCosts.setText(String.format(Locale.GERMANY, "%.2f € / Monat", total));
        });

        viewModel.allSubscriptions.observe(this, subscriptions -> {
            if (subscriptions != null) {
                adapter.setSubscriptions(subscriptions);
            }
        });
        viewModel.deleteExpiredCancelled();
    }

    private void setupNotifications() {
        NotificationHelper.createNotificationChannel(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
        scheduleNotificationWorker();
    }

    private void scheduleNotificationWorker() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                SubscriptionCheckWorker.class,
                1, TimeUnit.DAYS
        )
                .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "subscription_check",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        );
    }

    // ====================================
    // Utility Methods
    // ====================================

    private long calculateInitialDelay() {
        Calendar nextRun = Calendar.getInstance();
        nextRun.set(Calendar.HOUR_OF_DAY, 9);
        nextRun.set(Calendar.MINUTE, 0);
        nextRun.set(Calendar.SECOND, 0);
        nextRun.set(Calendar.MILLISECOND, 0);
        if (!nextRun.after(Calendar.getInstance())) {
            nextRun.add(Calendar.DAY_OF_MONTH, 1);
        }
        return nextRun.getTimeInMillis() - System.currentTimeMillis();
    }

    private void showDeleteDialog(Subscription subscription) {
        SubscriptionViewModel vm = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(R.string.delete_confirm_msg)
                .setPositiveButton(R.string.button_delete, (dialog, which) ->
                        vm.delete(subscription)
                )
                .setNegativeButton(R.string.button_cancel, null)
                .show();
    }
}

package com.dukenightshade.abowatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.model.Subscription;
import com.dukenightshade.abowatch.ui.adapter.OnSubscriptionActionListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Locale;

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
                // TODO: EditSubscriptionActivity starten
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
    }

    // ====================================
    // Utility Methods
    // ====================================

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

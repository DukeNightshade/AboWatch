package com.dukenightshade.abowatch.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.ui.SubscriptionViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SubscriptionViewModel viewModel;
    private SubscriptionAdapter adapter;
    private boolean editModeActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.activity.EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Views
        RecyclerView rvSubscriptions = findViewById(R.id.rvSubscriptions);
        TextView tvTotalCosts = findViewById(R.id.tvTotalCosts);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddSubscription);
        ExtendedFloatingActionButton fabEdit = findViewById(R.id.fabEditMode);

        // Adapter & RecyclerView
        adapter = new SubscriptionAdapter();
        rvSubscriptions.setLayoutManager(new LinearLayoutManager(this));
        rvSubscriptions.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);

        // Gesamtkosten beobachten
        viewModel.totalCosts.observe(this, total -> {
            if (total == null) total = 0.0;
            tvTotalCosts.setText(String.format(Locale.GERMANY, "%.2f € / Monat", total));
        });

        // Abo-Liste beobachten
        viewModel.allSubscriptions.observe(this, subscriptions -> {
            if (subscriptions != null) {
                adapter.setSubscriptions(subscriptions);
            }
        });

        // Edit-Modus toggle (ISO 9241-110: Steuerbarkeit)
        fabEdit.setOnClickListener(v -> {
            editModeActive = !editModeActive;
            adapter.setEditMode(editModeActive);
            fabEdit.setText(editModeActive ? "Fertig" : "Bearbeiten");
        });

        // Neues Abo hinzufügen (Platzhalter)
        fabAdd.setOnClickListener(v -> {
            // TODO: AddSubscriptionActivity starten
        });
    }
}

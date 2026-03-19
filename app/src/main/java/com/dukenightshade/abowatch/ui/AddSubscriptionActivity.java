package com.dukenightshade.abowatch.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.model.Subscription;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity zum Hinzufügen eines neuen Abonnements.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class AddSubscriptionActivity extends AppCompatActivity {

    // ====================================
    // Instance Variables
    // ====================================

    private TextInputEditText etName;
    private TextInputEditText etPrice;
    private TextInputEditText etCategory;
    private TextInputEditText etStartDate;
    private TextInputEditText etNoticePeriod;
    private SubscriptionViewModel viewModel;

    // ====================================
    // Lifecycle Methods
    // ====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);

        initViews();
        initViewModel();
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etStartDate = findViewById(R.id.etStartDate);
        etNoticePeriod = findViewById(R.id.etNoticePeriod);

        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveSubscription();
            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
    }

    private void saveSubscription() {
        String name = getTextOrEmpty(etName);
        double price = Double.parseDouble(getTextOrEmpty(etPrice).replace(",", "."));
        String category = getTextOrEmpty(etCategory);
        String startDate = getTextOrEmpty(etStartDate);
        int noticePeriod = Integer.parseInt(getTextOrEmpty(etNoticePeriod));

        Subscription subscription = new Subscription(name, price, category, startDate, noticePeriod);
        viewModel.insert(subscription);

        Toast.makeText(this, getString(R.string.toast_subscription_saved), Toast.LENGTH_SHORT).show();
        finish();
    }

    // ====================================
    // Utility Methods
    // ====================================

    private boolean validateInputs() {
        if (getTextOrEmpty(etName).isEmpty()) {
            etName.setError(getString(R.string.error_field_required));
            return false;
        }
        if (getTextOrEmpty(etPrice).isEmpty()) {
            etPrice.setError(getString(R.string.error_field_required));
            return false;
        }
        if (getTextOrEmpty(etCategory).isEmpty()) {
            etCategory.setError(getString(R.string.error_field_required));
            return false;
        }
        if (getTextOrEmpty(etStartDate).isEmpty()) {
            etStartDate.setError(getString(R.string.error_field_required));
            return false;
        }
        if (getTextOrEmpty(etNoticePeriod).isEmpty()) {
            etNoticePeriod.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    private String getTextOrEmpty(TextInputEditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }
}

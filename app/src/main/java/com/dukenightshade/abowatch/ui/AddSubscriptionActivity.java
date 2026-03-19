package com.dukenightshade.abowatch.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    private TextInputEditText etPriceEuros;
    private TextInputEditText etPriceCents;
    private AutoCompleteTextView etCategory;
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
        etName         = findViewById(R.id.etName);
        etPriceEuros   = findViewById(R.id.etPriceEuros);
        etPriceCents   = findViewById(R.id.etPriceCents);
        etCategory     = findViewById(R.id.etCategory);
        etStartDate    = findViewById(R.id.etStartDate);
        etNoticePeriod = findViewById(R.id.etNoticePeriod);

        String[] categories = getResources().getStringArray(R.array.subscription_categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categories
        );
        etCategory.setAdapter(categoryAdapter);

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
        String name      = getTextOrEmpty(etName);
        int euros        = Integer.parseInt(getTextOrEmpty(etPriceEuros).isEmpty() ? "0" : getTextOrEmpty(etPriceEuros));
        int cents        = Integer.parseInt(getTextOrEmpty(etPriceCents).isEmpty() ? "0" : getTextOrEmpty(etPriceCents));
        double price     = euros + (cents / 100.0);
        String category  = etCategory.getText().toString().trim();
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
        if (getTextOrEmpty(etPriceEuros).isEmpty()) {
            etPriceEuros.setError(getString(R.string.error_field_required));
            return false;
        }
        try {
            int euros = Integer.parseInt(getTextOrEmpty(etPriceEuros));
            int cents = getTextOrEmpty(etPriceCents).isEmpty() ? 0 : Integer.parseInt(getTextOrEmpty(etPriceCents));
            double price = euros + (cents / 100.0);
            if (price <= 0) {
                etPriceEuros.setError(getString(R.string.error_price_invalid));
                return false;
            }
        } catch (NumberFormatException e) {
            etPriceEuros.setError(getString(R.string.error_price_invalid));
            return false;
        }
        if (etCategory.getText().toString().trim().isEmpty()) {
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

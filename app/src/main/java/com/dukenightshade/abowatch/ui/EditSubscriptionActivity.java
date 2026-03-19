package com.dukenightshade.abowatch.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.model.Subscription;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity zum Bearbeiten eines bestehenden Abonnements.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class EditSubscriptionActivity extends AppCompatActivity {

    // ====================================
    // Constants
    // ====================================

    public static final String EXTRA_SUBSCRIPTION_ID = "subscription_id";

    // ====================================
    // Instance Variables
    // ====================================

    private TextInputEditText etName;
    private TextInputEditText etPrice;
    private TextInputEditText etCategory;
    private TextInputEditText etStartDate;
    private TextInputEditText etNoticePeriod;
    private SubscriptionViewModel viewModel;
    private int subscriptionId;

    // ====================================
    // Lifecycle Methods
    // ====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscription);

        subscriptionId = getIntent().getIntExtra(EXTRA_SUBSCRIPTION_ID, -1);
        if (subscriptionId == -1) {
            finish();
            return;
        }

        initViews();
        initViewModel();
        loadSubscription();
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    private void initViews() {
        etName         = findViewById(R.id.etName);
        etPrice        = findViewById(R.id.etPrice);
        etCategory     = findViewById(R.id.etCategory);
        etStartDate    = findViewById(R.id.etStartDate);
        etNoticePeriod = findViewById(R.id.etNoticePeriod);

        MaterialButton btnSave   = findViewById(R.id.btnSave);
        MaterialButton btnDelete = findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveSubscription();
            }
        });
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
    }

    private void loadSubscription() {
        viewModel.getById(subscriptionId).observe(this, sub -> {
            if (sub == null) { finish(); return; }
            etName.setText(sub.getName());
            etPrice.setText(String.valueOf(sub.getPrice()));
            etCategory.setText(sub.getCategory());
            etStartDate.setText(sub.getStartDate());
            etNoticePeriod.setText(String.valueOf(sub.getNoticePeriod()));
        });
    }

    private void saveSubscription() {
        String name     = getTextOrEmpty(etName);
        double price    = Double.parseDouble(getTextOrEmpty(etPrice).replace(",", "."));
        String category = getTextOrEmpty(etCategory);
        String date     = getTextOrEmpty(etStartDate);
        int notice      = Integer.parseInt(getTextOrEmpty(etNoticePeriod));

        Subscription updated = new Subscription(name, price, category, date, notice);
        updated.setId(subscriptionId);
        viewModel.update(updated);

        Toast.makeText(this, getString(R.string.toast_subscription_saved), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_confirm_title))
                .setMessage(getString(R.string.delete_confirm_msg))
                .setPositiveButton(getString(R.string.button_delete), (d, w) -> deleteSubscription())
                .setNegativeButton(getString(R.string.button_cancel), null)
                .show();
    }

    private void deleteSubscription() {
        viewModel.deleteById(subscriptionId);
        Toast.makeText(this, getString(R.string.toast_subscription_deleted), Toast.LENGTH_SHORT).show();
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

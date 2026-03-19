package com.dukenightshade.abowatch.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.dukenightshade.abowatch.R;
import com.dukenightshade.abowatch.model.Subscription;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
    private TextInputEditText etPriceEuros;
    private TextInputEditText etPriceCents;
    private AutoCompleteTextView etCategory;
    private AutoCompleteTextView etBillingCycle;
    private TextInputEditText etStartDate;
    private TextInputEditText etNoticePeriod;
    private SubscriptionViewModel viewModel;
    private int subscriptionId;
    private MaterialCheckBox cbCancelled;

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
        etPriceEuros   = findViewById(R.id.etPriceEuros);
        etPriceCents   = findViewById(R.id.etPriceCents);
        etCategory     = findViewById(R.id.etCategory);
        etBillingCycle = findViewById(R.id.etBillingCycle);
        etStartDate    = findViewById(R.id.etStartDate);
        etNoticePeriod = findViewById(R.id.etNoticePeriod);
        cbCancelled    = findViewById(R.id.cbCancelled);

        String[] categories = getResources().getStringArray(R.array.subscription_categories);
        etCategory.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, categories));

        String[] billingCycles = getResources().getStringArray(R.array.billing_cycles);
        etBillingCycle.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, billingCycles));

        etStartDate.setFocusable(false);
        etStartDate.setClickable(true);
        etStartDate.setOnClickListener(v -> showDatePicker());

        MaterialButton btnSave   = findViewById(R.id.btnSave);
        MaterialButton btnDelete = findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) saveSubscription();
        });
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.label_start_date))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            String date = String.format(Locale.ROOT, "%02d.%02d.%04d",
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.YEAR));
            etStartDate.setText(date);
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void loadSubscription() {
        viewModel.getById(subscriptionId).observe(this, sub -> {
            if (sub == null) { finish(); return; }
            etName.setText(sub.getName());

            int euros = (int) sub.getPrice();
            int cents = (int) Math.round((sub.getPrice() - euros) * 100);
            etPriceEuros.setText(String.valueOf(euros));
            etPriceCents.setText(String.format(Locale.ROOT, "%02d", cents));

            etCategory.setText(sub.getCategory(), false);
            etStartDate.setText(sub.getStartDate());
            etNoticePeriod.setText(String.valueOf(sub.getNoticePeriod()));
            etBillingCycle.setText(keyToLabel(sub.getBillingCycle()), false);
            cbCancelled.setChecked(sub.isCancelled());
        });
    }

    private void saveSubscription() {
        String name        = getTextOrEmpty(etName);
        int euros          = Integer.parseInt(getTextOrEmpty(etPriceEuros).isEmpty() ? "0" : getTextOrEmpty(etPriceEuros));
        int cents          = Integer.parseInt(getTextOrEmpty(etPriceCents).isEmpty() ? "0" : getTextOrEmpty(etPriceCents));
        double price       = euros + (cents / 100.0);
        String category    = etCategory.getText().toString().trim();
        String date        = getTextOrEmpty(etStartDate);
        int notice         = Integer.parseInt(getTextOrEmpty(etNoticePeriod));
        String cycleLabel  = etBillingCycle.getText().toString().trim();
        String billingCycle = cycleToKey(cycleLabel);

        Subscription updated = new Subscription(name, price, category, date, notice, billingCycle);
        updated.setId(subscriptionId);
        updated.setIsCancelled(cbCancelled.isChecked());
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
        if (getTextOrEmpty(etPriceEuros).isEmpty()) {
            etPriceEuros.setError(getString(R.string.error_field_required));
            return false;
        }
        try {
            int euros = Integer.parseInt(getTextOrEmpty(etPriceEuros));
            int cents = getTextOrEmpty(etPriceCents).isEmpty() ? 0 : Integer.parseInt(getTextOrEmpty(etPriceCents));
            if (euros + (cents / 100.0) <= 0) {
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
        if (etBillingCycle.getText().toString().trim().isEmpty()) {
            etBillingCycle.setError(getString(R.string.error_field_required));
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

    /**
     * Wandelt den lokalisierten Anzeigewert in den DB-Schlüssel um.
     */
    private String cycleToKey(String label) {
        String[] cycles = getResources().getStringArray(R.array.billing_cycles);
        if (label.equals(cycles[1])) return Subscription.BILLING_CYCLE_YEARLY;
        return Subscription.BILLING_CYCLE_MONTHLY;
    }

    /**
     * Wandelt den DB-Schlüssel in den lokalisierten Anzeigewert um.
     */
    private String keyToLabel(String key) {
        String[] cycles = getResources().getStringArray(R.array.billing_cycles);
        if (Subscription.BILLING_CYCLE_YEARLY.equals(key)) return cycles[1];
        return cycles[0];
    }

    private String getTextOrEmpty(TextInputEditText field) {
        return field.getText() != null ? field.getText().toString().trim() : "";
    }
}

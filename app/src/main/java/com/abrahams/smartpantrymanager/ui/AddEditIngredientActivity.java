package com.abrahams.smartpantrymanager.ui;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.abrahams.smartpantrymanager.data.DatabaseHelper;
import com.abrahams.smartpantrymanager.models.PantryItem;
import com.example.smartpantrymanager.R;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";

    private int itemId = -1;

    // sets up the form, unit list, the date picker and the save button.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.addEditRoot), (view, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    Insets keyboard = insets.getInsets(
                            WindowInsetsCompat.Type.ime());

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            Math.max(systemBars.bottom, keyboard.bottom));

                    return insets;
                });

        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);

        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);

        String[] units = {"g", "kg", "ml", "l", "pcs", "tsp"};

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units);

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinnerUnit.setAdapter(unitAdapter);

        EditText editExpiryDate = findViewById(R.id.editExpiryDate);

        editExpiryDate.setOnClickListener(view -> {
            Calendar today = Calendar.getInstance();

            DatePickerDialog picker = new DatePickerDialog(
                    this,
                    (datePicker, year, month, day) -> {
                        String date = String.format(
                                Locale.ROOT,
                                "%04d-%02d-%02d",
                                year, month + 1, day);

                        editExpiryDate.setText(date);
                    },
                    today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH),
                    today.get(Calendar.DAY_OF_MONTH));

            picker.show();
        });
        findViewById(R.id.buttonSave).setOnClickListener(
                view -> saveIngredient());

        if (savedInstanceState == null && itemId != -1) {
            loadIngredient();
        }
    }

    // loads the item from the database and fills in the form when an item is edited.
    private void loadIngredient() {
        try (DatabaseHelper helper = new DatabaseHelper(this)) {
            PantryItem item = helper.getPantryItemById(itemId);

            if (item == null) {
                Toast.makeText(
                        this, R.string.ingredient_no_longer_exists,
                        Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            EditText editName = findViewById(R.id.editIngredientName);
            EditText editQuantity = findViewById(R.id.editQuantity);
            EditText editExpiry = findViewById(R.id.editExpiryDate);
            Spinner spinnerUnit = findViewById(R.id.spinnerUnit);

            editName.setText(item.getName());
            String quantity = BigDecimal.valueOf(item.getQuantity())
                    .stripTrailingZeros()
                    .toPlainString();

            editQuantity.setText(quantity);
            editExpiry.setText(
                    item.getExpiryDate() == null ? "" : item.getExpiryDate());

            for (int position = 0;
                 position < spinnerUnit.getCount(); position++) {
                if (item.getUnit().equals(
                        spinnerUnit.getItemAtPosition(position).toString())) {
                    spinnerUnit.setSelection(position);
                    break;
                }
            }
        } catch (SQLiteException exception) {
            Toast.makeText(
                    this, R.string.could_not_load_ingredient,
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    // checks the input and then adds a new item or updates the item that is there.
    private void saveIngredient() {
        EditText editName = findViewById(R.id.editIngredientName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        EditText editExpiry = findViewById(R.id.editExpiryDate);
        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);

        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError(getString(R.string.error_enter_name));
            editName.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException exception) {
            editQuantity.setError(
                    getString(R.string.error_valid_quantity));
            editQuantity.requestFocus();
            return;
        }

        if (Double.isNaN(quantity)
                || Double.isInfinite(quantity)
                || quantity <= 0) {
            editQuantity.setError(
                    getString(R.string.error_quantity_above_zero));
            editQuantity.requestFocus();
            return;
        }

        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = editExpiry.getText().toString().trim();

        PantryItem item = new PantryItem(
                name,
                quantity,
                unit,
                expiry.isEmpty() ? null : expiry);

        try (DatabaseHelper helper = new DatabaseHelper(this)) {
            boolean saved;

            if (itemId == -1) {
                saved = helper.insertPantryItem(item) != -1;
            } else {

                item.setId(itemId);
                saved = helper.updatePantryItem(item) == 1;
            }

            if (!saved) {
                Toast.makeText(
                        this,
                        R.string.could_not_save_ingredient,
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (SQLiteException exception) {
            Toast.makeText(
                    this,
                    R.string.could_not_save_ingredient,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(
                this,
                R.string.ingredient_saved,
                Toast.LENGTH_SHORT).show();

        finish();
    }
}

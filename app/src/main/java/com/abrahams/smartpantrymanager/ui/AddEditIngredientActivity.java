package com.abrahams.smartpantrymanager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.R;
import com.abrahams.smartpantrymanager.data.DatabaseHelper;
import com.abrahams.smartpantrymanager.models.PantryItem;

import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    public static String EXTRA_ITEM_ID = "item_id";

    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

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
    }

    private void saveIngredient() {
        EditText editName = findViewById(R.id.editIngredientName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        EditText editExpiry = findViewById(R.id.editExpiryDate);
        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);

        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Enter an ingredient name");
            editName.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException exception) {
            editQuantity.setError("Enter a valid quantity");
            editQuantity.requestFocus();
            return;
        }

        if (Double.isNaN(quantity)
                || Double.isInfinite(quantity)
                || quantity <= 0) {
            editQuantity.setError("Enter a quantity greater than zero");
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

        try (DatabaseHelper helper = new  DatabaseHelper(this)) {
            long newId = helper.insertPantryItem(item);

            if (newId == -1) {
                Toast.makeText(
                        this,
                        "Could not save ingredient",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (SQLiteException exception) {
            Toast.makeText(
                    this,
                    "Could not save ingredient",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(
                this,
                "Ingredient saved",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}

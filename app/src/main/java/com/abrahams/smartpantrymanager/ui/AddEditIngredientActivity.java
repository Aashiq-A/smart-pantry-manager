package com.abrahams.smartpantrymanager.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

// new import.
import android.app.DatePickerDialog;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.R;

import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

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
    }
}

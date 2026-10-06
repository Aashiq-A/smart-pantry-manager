package com.abrahams.smartpantrymanager.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import com.example.smartpantrymanager.R;

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
    }
}

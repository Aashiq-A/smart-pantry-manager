package com.abrahams.smartpantrymanager.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

// new import added.
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.abrahams.smartpantrymanager.data.DatabaseHelper;
import com.abrahams.smartpantrymanager.adapters.PantryAdapter;

public class PantryFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private PantryAdapter adapter;
    private TextView textEmptyPantry;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_pantry, container, false);
    }


}

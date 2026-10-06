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

// new import added for ingredients.
import com.abrahams.smartpantrymanager.models.PantryItem;

import java.util.ArrayList;
import java.util.List;

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

    // created new override that connects the database.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(requireContext());

        textEmptyPantry = view.findViewById(R.id.textEmptyPantry);

        RecyclerView recyclerPantry =
                view.findViewById(R.id.recyclerPantry);

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        adapter = new PantryAdapter(new ArrayList<>(), null);
        recyclerPantry.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            loadPantryItems();
        }
    }

    private void loadPantryItems() {
        List<PantryItem> items = databaseHelper.getAllPantryItems();

        adapter.updateItems(items);

        textEmptyPantry.setVisibility(
                items.isEmpty() ? View.VISIBLE : View.GONE);
    }
}

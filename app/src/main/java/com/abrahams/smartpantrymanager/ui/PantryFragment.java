package com.abrahams.smartpantrymanager.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.abrahams.smartpantrymanager.data.DatabaseHelper;
import com.abrahams.smartpantrymanager.adapters.PantryAdapter;
import com.abrahams.smartpantrymanager.models.PantryItem;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment
        implements PantryAdapter.OnItemActionListener {

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

        adapter = new PantryAdapter(new ArrayList<>(), this);
        recyclerPantry.setAdapter(adapter);

        view.findViewById(R.id.fabAddItem).setOnClickListener(button -> {
            Intent intent = new Intent(
                    requireContext(), AddEditIngredientActivity.class);

            startActivity(intent);
        });

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

    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(
                requireContext(), AddEditIngredientActivity.class);
        intent.putExtra(
                AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete ingredient")
                .setMessage("Delete " + item.getName() + "?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", ((dialog, which) -> {
                    try {
                        int deleted = databaseHelper.deletePantryItem(
                                item.getId());

                        Toast.makeText(
                                requireContext(),
                                deleted == 1
                                        ? "Ingredient deleted"
                                        : "Ingredient no longer exists",
                                Toast.LENGTH_SHORT).show();
                        loadPantryItems();
                    } catch (SQLiteException exception) {
                        Toast.makeText(
                                requireContext(),
                                "Could not delete ingredient",
                                Toast.LENGTH_SHORT.show();
                    }
                })
                        .show();
    }
}

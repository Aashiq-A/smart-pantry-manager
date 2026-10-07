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

    // this method reloads the pantry every time the screen shows again.
    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null && databaseHelper != null) {
            loadPantryItems();
        }
    }
    // read all the pantry items from the database.
    private void loadPantryItems() {
        try {
            List<PantryItem> items =
                    databaseHelper.getAllPantryItems();

            adapter.updateItems(items);

            textEmptyPantry.setVisibility(
                    items.isEmpty() ? View.VISIBLE : View.GONE);

        } catch (SQLiteException exception) {
            Toast.makeText(
                    requireContext(),
                    R.string.could_not_load_pantry,
                    Toast.LENGTH_SHORT).show();
        }
    }

    // opens the edit screen and passes the id of the item that was tapped.
    @Override
    public void onItemClick(PantryItem item) {
        Intent intent = new Intent(
                requireContext(), AddEditIngredientActivity.class);
        intent.putExtra(
                AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    // asks the user to confirm before the ingredient it gets deleted.
    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_item)
                .setMessage(getString(
                        R.string.delete_ingredient_message,
                        item.getName()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {

                    if (databaseHelper == null) {
                        return;
                    }

                    try {
                        int deleted = databaseHelper.deletePantryItem(
                                item.getId());

                        Toast.makeText(
                                requireContext(),
                                deleted == 1
                                        ? R.string.ingredient_deleted
                                        : R.string.ingredient_no_longer_exists,
                                Toast.LENGTH_SHORT).show();

                        loadPantryItems();

                    } catch (SQLiteException exception) {
                        Toast.makeText(
                                requireContext(),
                                R.string.could_not_delete_ingredient,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    // new database view cleanup.
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (databaseHelper != null) {
            databaseHelper.close();
            databaseHelper = null;
        }

        adapter = null;
        textEmptyPantry = null;
    }
}


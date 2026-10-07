package com.abrahams.smartpantrymanager.ui;

import android.content.Intent;
import android.widget.TextView;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.abrahams.smartpantrymanager.models.PantryItem;
import com.abrahams.smartpantrymanager.models.Recipe;
import com.abrahams.smartpantrymanager.util.RecipeMatcher;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abrahams.smartpantrymanager.adapters.RecipeAdapter;
import com.abrahams.smartpantrymanager.data.DatabaseHelper;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartpantrymanager.R;

public class RecipesFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private RecipeAdapter adapter;
    private TextView textEmptyRecipes;

    @Nullable
    @Override
    public View onCreateView(
            @Nullable LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(requireContext());

        textEmptyRecipes = view.findViewById(
                R.id.textEmptyRecipes);

        RecyclerView recyclerRecipes = view.findViewById(
                R.id.recyclerRecipes);

        recyclerRecipes.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        adapter = new RecipeAdapter(new ArrayList<>(), null);
        recyclerRecipes.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null && databaseHelper != null) {
            loadMatchingRecipes();
        }
    }

    private void loadMatchingRecipes() {
        try {
            List<Recipe> recipes =
                    databaseHelper.getAllRecipes();

            List<PantryItem> pantryItems =
                    databaseHelper.getAllPantryItems();

            List<Recipe> matches =
                    RecipeMatcher.findMatchingRecipes(
                            recipes, pantryItems);

            adapter.updateRecipes(matches);

            textEmptyRecipes.setVisibility(
                    matches.isEmpty() ? View.VISIBLE : View.GONE);

        } catch (SQLiteException exception) {
            Toast.makeText(
                    requireContext(),
                    R.string.could_not_load_recipes,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (databaseHelper != null) {
            databaseHelper.close();
            databaseHelper = null;
        }

        adapter = null;
        textEmptyRecipes = null;
    }
}
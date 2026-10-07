package com.abrahams.smartpantrymanager.ui;

import android.widget.TextView;

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
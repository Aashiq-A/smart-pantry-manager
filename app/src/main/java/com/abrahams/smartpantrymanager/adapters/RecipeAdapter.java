package com.abrahams.smartpantrymanager.adapters;

import com.abrahams.smartpantrymanager.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes;
    private final OnRecipeClickListener listener;

    public RecipeAdapter(
            List<Recipe> recipes,
            OnRecipeClickListener listener) {

        this.recipes = new ArrayList<>(recipes);
        this.listener = listener;
    }
}

package com.abrahams.smartpantrymanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abrahams.smartpantrymanager.models.Recipe;
import com.example.smartpantrymanager.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {


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

    // PART 3: Create recipe cards and connect their data.

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        Recipe recipe = recipes.get(position);

        holder.textRecipeName.setText(recipe.getName());

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onRecipeClick(recipe);
            }
        });
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        final TextView textRecipeName;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            textRecipeName = itemView.findViewById(
                    R.id.textRecipeName);
        }
    }
}
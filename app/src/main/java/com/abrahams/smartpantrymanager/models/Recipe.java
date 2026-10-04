package com.abrahams.smartpantrymanager.models;

import java.util.ArrayList;
import java.util.List;

// a recipe name stored, cooking steps, and the ingredients that is needed.
public class Recipe {

    private int id;
    private String name;
    private String instructions;
    private List<RecipeIngredient> ingredients;

    // recipe will be created that has an empty ingredient list and be added after.
    public Recipe(int id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = new ArrayList<>();
    }

    // methods will let the other classes read the details of the recipe.
    public int getId() { return id; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }

    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }
}

package com.abrahams.smartpantrymanager.models;

// only 1 ingredient that is needed by the recipe.
public class RecipeIngredient {

    private int id;
    private int recipeId;
    private String name;
    private double quantity;
    private String unit;

    // object that is created with the ingredient information.
    public RecipeIngredient(int id, int recipeId, String name, double quantity, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    // methods that let other classes read the ingredient information.
    public int getId() { return id; }
    public int getRecipeId() { return recipeId; }
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
}

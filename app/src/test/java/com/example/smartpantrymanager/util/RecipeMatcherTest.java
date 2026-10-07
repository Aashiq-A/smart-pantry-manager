package com.example.smartpantrymanager.util;

import com.abrahams.smartpantrymanager.models.PantryItem;
import com.abrahams.smartpantrymanager.models.Recipe;
import com.abrahams.smartpantrymanager.models.RecipeIngredient;
import com.abrahams.smartpantrymanager.util.RecipeMatcher;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecipeMatcherTest {

    @Test
    public void acceptsExactQuantitiesAndNormalizedNames() {
        Recipe recipe = recipeWith("tomato", 2, "pcs");

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("  TOMATOES  ", 2, "pcs", null));

        assertTrue(RecipeMatcher.canMake(recipe, pantry));
    }

    @Test
    public void rejectsMissingIngredient() {
        Recipe recipe = recipeWith("tomato", 2, "pcs");

        recipe.addIngredient(
                new RecipeIngredient(2, 1, "onion", 1, "pcs"));

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("tomato", 2, "pcs", null));

        assertFalse(RecipeMatcher.canMake(recipe, pantry));
    }

    @Test
    public void rejectsInsufficientQuantity() {
        Recipe recipe = recipeWith("tomato", 3, "pcs");

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("tomato", 2, "pcs", null));

        assertFalse(RecipeMatcher.canMake(recipe, pantry));
    }

    @Test
    public void convertsKilogramsToGrams() {
        Recipe recipe = recipeWith("flour", 500, "g");

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("flour", 0.5, "kg", null));

        assertTrue(RecipeMatcher.canMake(recipe, pantry));
    }

    @Test
    public void convertLitresAndTeaspoonsToMillilitres() {
        Recipe milkRecipe = recipeWith("milk", 250, "ml");

        List<PantryItem> milkPantry = Collections.singletonList(
                new PantryItem("milk", 0.25, "l", null));

        assertTrue(RecipeMatcher.canMake(milkRecipe, milkPantry));

        Recipe oilRecipe = recipeWith("oil", 2, "tsp");

        List<PantryItem> oilPantry = Collections.singletonList(
                new PantryItem("oil", 10, "ml", null));

        assertTrue(RecipeMatcher.canMake(oilRecipe, oilPantry));
    }

    @Test
    public void combinesDuplicatePantryEntries() {
        Recipe recipe = recipeWith("flour", 500, "g");

        List<PantryItem> pantry = Arrays.asList(
                new PantryItem("flour", 200, "g", null),
                new PantryItem(" FLOUR ", 0.3, "kg", null));

        assertTrue(RecipeMatcher.canMake(recipe, pantry));
    }

    private static Recipe recipeWith(
            String name,
            double quantity,
            String unit) {

        Recipe recipe = new Recipe(1, "Test recipe", "Cook");

        recipe.addIngredient(
                new RecipeIngredient(1, 1, name, quantity, unit));

        return recipe;
    }
}
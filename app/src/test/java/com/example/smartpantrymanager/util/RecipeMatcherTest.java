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
    public void () {
        Recipe recipe = ("tomato", 2, "pcs");

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("  TOMATOES  ", 2, "pcs", null));

        assertTrue(RecipeMatcher.canMake(recipe, pantry));
    }

    @Test
    public void () {
        Recipe recipe = ("tomato", 2, "pcs");

        recipe.addIngredient(
                new RecipeIngredient(2, 1, "onion", 1, "pcs"));

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("tomato", 2, "pcs", null))

        assertFalse(RecipeMatcher.canMake(recipe, pantry))
    }

    @Test
    public void () {
        Recipe recipe = ("tomato", 3, "pcs");

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("tomato", 2, "pcs", null))

        assertFalse(RecipeMatcher.canMake(recipe, pantry))
    }

    private static Recipe (
            String name,
            double quantity,
            String unit) {

        Recipe recipe = new Recipe(1, "Test recipe", "Cook")

        recipe.addIngredient(
                new RecipeIngredient(1, 1, name, quantity, unit))

        return recipe
    }
}
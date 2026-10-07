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

    @Test
    public void combinesRepeatedRecipeRequirements() {
        Recipe recipe = recipeWith("tomato", 2, "pcs");

        recipe.addIngredient(
                new RecipeIngredient(2, 1, "tomatoes", 2, "pcs"));

        List<PantryItem> insufficient = Collections.singletonList(
                new PantryItem("tomato", 3, "pcs", null));

        assertFalse(RecipeMatcher.canMake(recipe, insufficient));

        List<PantryItem> enough = Collections.singletonList(
                new PantryItem("tomato", 4, "pcs", null));

        assertTrue(RecipeMatcher.canMake(recipe, enough));
    }

    @Test
    public void keepsWeightVolumeAndCountSeparate() {
        Recipe recipe = recipeWith("milk", 100, "ml");

        List<PantryItem> weightPantry = Collections.singletonList(
                new PantryItem("milk", 100, "g", null));

        List<PantryItem> countPantry = Collections.singletonList(
                new PantryItem("milk", 100, "pcs", null));

        assertFalse(RecipeMatcher.canMake(recipe, weightPantry));
        assertFalse(RecipeMatcher.canMake(recipe, countPantry));
    }
    @Test
    public void rejectsUnknownUnitsAndInvalidPantryQuantities() {
        Recipe recipe = recipeWith("flour", 100, "g");

        List<PantryItem> unknownUnit = Collections.singletonList(
                new PantryItem("flour", 100, "unknown", null));

        assertFalse(RecipeMatcher.canMake(recipe, unknownUnit));

        double[] invalidQuantities = {
                0,
                -1,
                Double.NaN,
                Double.POSITIVE_INFINITY
        };

        for (double quantity : invalidQuantities) {
            List<PantryItem> pantry = Collections.singletonList(
                    new PantryItem("flour", quantity, "g", null));

            assertFalse(RecipeMatcher.canMake(recipe, pantry));
        }

        Recipe unknownRequirement = recipeWith("flour", 1, "unknown");

        List<PantryItem> validPantry = Collections.singletonList(
                new PantryItem("flour", 100, "g", null));

        assertFalse(RecipeMatcher.canMake(
                unknownRequirement, validPantry));
    }

    @Test
    public void rejectsEmptyRecipesAndInvalidRequirements() {
        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("flour", 100, "g", null));

        Recipe emptyRecipe = new Recipe(1, "Empty recipe", "Cook");

        assertFalse(RecipeMatcher.canMake(emptyRecipe, pantry));
        assertFalse(RecipeMatcher.canMake(null, pantry));

        assertFalse(RecipeMatcher.canMake(
                recipeWith("flour", 100, "g"), null));

        assertFalse(RecipeMatcher.canMake(
                recipeWith("flour", 0, "g"), pantry));

        assertFalse(RecipeMatcher.canMake(
                recipeWith("flour", Double.NaN, "g"), pantry));
    }

    @Test
    public void returnsOnlyRecipesThatCanBeMade() {
        Recipe availableRecipe = recipeWith("tomato", 2, "pcs");
        Recipe unavailableRecipe = recipeWith("onion", 1, "pcs");

        List<PantryItem> pantry = Collections.singletonList(
                new PantryItem("tomato", 2, "pcs", null));

        List<Recipe> matches = RecipeMatcher.findMatchingRecipes(
                Arrays.asList(availableRecipe, unavailableRecipe),
                pantry);

        assertEquals(1, matches.size());
        assertEquals(availableRecipe, matches.get(0));

        assertTrue(RecipeMatcher.findMatchingRecipes(
                null, pantry).isEmpty());
    }
    // a new test for missing ingredient.
    @Test
    public void rejectsRecipeWhenOnlyFourOfFiveIngredientsArePresent() {
        Recipe recipe = new Recipe(
                1, "Five ingredient recipe", "Cook");

        recipe.addIngredient(
                new RecipeIngredient(1, 1, "onion", 1, "pcs"));
        recipe.addIngredient(
                new RecipeIngredient(2, 1, "tomato", 2, "pcs"));
        recipe.addIngredient(
                new RecipeIngredient(3, 1, "carrot", 1, "pcs"));
        recipe.addIngredient(
                new RecipeIngredient(4, 1, "oil", 15, "ml"));
        recipe.addIngredient(
                new RecipeIngredient(5, 1, "curry powder", 1, "tsp"));

        List<PantryItem> fourOfFive = Arrays.asList(
                new PantryItem("Onions", 2, "pcs", null),
                new PantryItem("Tomatoes", 4, "pcs", null),
                new PantryItem("Carrots", 3, "pcs", null),
                new PantryItem("Oil", 0.5, "l", null));

        assertFalse(RecipeMatcher.canMake(recipe, fourOfFive));

        assertTrue(RecipeMatcher.findMatchingRecipes(
                Collections.singletonList(recipe),
                fourOfFive).isEmpty());

        List<PantryItem> allFive = Arrays.asList(
                new PantryItem("Onions", 2, "pcs", null),
                new PantryItem("Tomatoes", 4, "pcs", null),
                new PantryItem("Carrots", 3, "pcs", null),
                new PantryItem("Oil", 0.5, "l", null),
                new PantryItem("Curry powder", 2, "tsp", null));


        assertTrue(RecipeMatcher.canMake(recipe, allFive));
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
package com.abrahams.smartpantrymanager.util;

import com.abrahams.smartpantrymanager.models.PantryItem;
import com.abrahams.smartpantrymanager.models.Recipe;
import com.abrahams.smartpantrymanager.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public final class RecipeMatcher {

    private RecipeMatcher() {
    }

    public static List<Recipe> findMatchingRecipes(
            List<Recipe> recipes,
            List<PantryItem> pantryItems) {

        List<Recipe> matches = new ArrayList<>();

        if (recipes == null || pantryItems == null) {
            return matches;
        }

        for (Recipe recipe : recipes) {
            if (canMake(recipe, pantryItems)) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static boolean canMake(
            Recipe recipe,
            List<PantryItem> pantryItems) {

        if (recipe == null
                || pantryItems == null
                || recipe.getIngredients() == null
                || recipe.getIngredients().isEmpty()) {
            return false;
        }

        Map<String, Double> available = new HashMap<>();
        Map<String, Double> required = new HashMap<>();


        for (PantryItem item : pantryItems) {
            if (item == null) {
                continue;
            }

            String key = ingredientKey(item.getName(), item.getUnit());

            double quantity = UnitConverter.toBaseQuantity(
                    item.getQuantity(), item.getUnit());

            if (!key.isEmpty() && isPositiveFinite(quantity)) {
                addQuantity(available, key, quantity);
            }
        }

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            if (ingredient == null) {
                return false;
            }

            String key = ingredientKey(
                    ingredient.getName(), ingredient.getUnit());

            double quantity = UnitConverter.toBaseQuantity(
                    ingredient.getQuantity(), ingredient.getUnit());

            if (key.isEmpty() || !isPositiveFinite(quantity)) {
                return false;
            }

            addQuantity(required, key, quantity);
        }

        for (Map.Entry<String, Double> entry : required.entrySet()) {
            Double stock = available.get(entry.getKey());
            double needed = entry.getValue();

            if (stock == null
                    || !isPositiveFinite(stock)
                    || !isPositiveFinite(needed)
                    || stock < needed) {
                return false;
            }
        }

        return true;
    }

    private static String ingredientKey(String name, String unit) {
        String normalizedName = IngredientNormalizer.normalize(name);
        String baseUnit = UnitConverter.baseUnit(unit);

        if (normalizedName.isEmpty() || baseUnit.isEmpty()) {
            return "";
        }

        return normalizedName + "\n" + baseUnit;
    }

    private static void addQuantity(
            Map<String, Double> quantities,
            String key,
            double quantity) {

        Double existing = quantities.get(key);

        quantities.put(
                key,
                existing == null ? quantity : existing + quantity);
    }

    private static Boolean isPositiveFinite(double quantity) {
        return !Double.isNaN(quantity)
                && !Double.isInfinite(quantity)
                && quantity > 0;
    }
}


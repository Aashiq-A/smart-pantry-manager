package com.example.smartpantrymanager;

import  com.abrahams.smartpantrymanager.util.IngredientNormalizer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IngredientNormalizerTest {

    @Test
    public void normalizesCapitalLettersAndSpaces() {
        assertEquals(
                "green pepper",
                IngredientNormalizer.normalize(" GREEN PEPPER "));
    }

    @Test
    public void normalizesCommonPlurals() {
        assertEquals(
                "tomato",
                IngredientNormalizer.normalize("Tomatoes"));

        assertEquals(
                "potato",
                IngredientNormalizer.normalize("Potatoes"));

        assertEquals(
                "green pepper",
                IngredientNormalizer.normalize("green peppers"));

        assertEquals(
                "cooked bean",
                IngredientNormalizer.normalize("cooked beans"));
    }

    @Test
    public void preservesOtherNamesAndHandlesEmptyInput() {
        assertEquals(
                "rice",
                IngredientNormalizer.normalize("rice"));

        assertEquals(
                "oats",
                IngredientNormalizer.normalize("oats"));

        assertEquals("", IngredientNormalizer.normalize(null));
        assertEquals("", IngredientNormalizer.normalize(" "));
    }
}

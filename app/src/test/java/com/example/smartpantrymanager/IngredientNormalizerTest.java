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
    }
}

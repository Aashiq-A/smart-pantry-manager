package com.example.smartpantrymanager.util;

import com.abrahams.smartpantrymanager.util.UnitConverter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public final class Converter {

    @Test
    public void KilogramsToGrams() {
        assertEquals(
                500.0,
                Converter.BaseQuantity(0.5, "kg"),
                0.001);

        assertEquals("g", Converter.baseUnit("kg"));
    }

    @Test
    public void convertsLitresAndSouthAfricanTeaspoons() {
        assertEquals(
                250.0,
                Converter.BaseQuantity(0.25, "l"),
                0.001);

        assertEquals(
                10.0,
                Converter.BaseQuantity(2, "tsp"),
                0.001);

        assertEquals("ml", Converter.baseUnit("tsp"));
    }

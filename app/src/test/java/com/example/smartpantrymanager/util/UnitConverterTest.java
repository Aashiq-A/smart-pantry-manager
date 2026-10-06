package com.example.smartpantrymanager.util;

import com.abrahams.smartpantrymanager.util.UnitConverter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class UnitConverterTest {

    @Test
    public void convertKilogramsToGrams() {
        assertEquals(
                500.0,
                UnitConverter.toBaseQuantity(0.5, "kg"),
                0.001);

        assertEquals("g", UnitConverter.baseUnit("kg"));
    }

    @Test
    public void convertsLitresAndSouthAfricanTeaspoons() {
        assertEquals(
                250.0,
                UnitConverter.toBaseQuantity(0.25, "l"),
                0.001);

        assertEquals(
                10.0,
                UnitConverter.toBaseQuantity(2, "tsp"),
                0.001);

        assertEquals("ml", UnitConverter.baseUnit("tsp"));
    }

    @Test
    public void keepsWeightVolumeAndCountSeparate() {
        assertNotEquals(
                UnitConverter.baseUnit("g"),
                UnitConverter.baseUnit("ml"));

        assertNotEquals(
                UnitConverter.baseUnit("pcs"),
                UnitConverter.baseUnit("g"));

        assertEquals(
                3.0,
                UnitConverter.toBaseQuantity(3, "pcs"),
                0.001);
    }

    @Test
    public void handlesUnitSpacingAndRejectsUnknownUnits() {
        assertEquals(
                1000.0,
                UnitConverter.toBaseQuantity(1, " KG "),
                0.001);

        assertEquals("", UnitConverter.baseUnit("unknown"));

        assertTrue(Double.isNaN(
                UnitConverter.toBaseQuantity(1, "unknown")));
    }
}

package com.abrahams.smartpantrymanager.util;

import java.util.Locale;

public final class UnitConverter {

    private UnitConverter() {
    }
    // gives the base unit for each unit, weight it is g, volume it is ml and items it is pcs.
    public static String baseUnit(String unit) {
        switch (clean(unit)) {
            case "g":
            case "kg":
                return "g";

            case "ml":
            case "l":
            case "tsp":
                return "ml";

            case "pcs":
                return "pcs";

            default:
                return "";
        }
    }
    // changes the quantity into the base unit, kg and l it is times 1000 and tsp it is times 5.
    public static double toBaseQuantity(double quantity, String unit) {
        switch (clean(unit)) {
            case "kg":
            case "l":
                return quantity * 1000;

            case "tsp":
                return quantity * 5;

            case "g":
            case "ml":
            case "pcs":
                return quantity;

            default:
                return Double.NaN;
        }
    }

    private static String clean(String unit) {
        return unit == null
                ? ""
                : unit.trim().toLowerCase(Locale.ROOT);
    }
}
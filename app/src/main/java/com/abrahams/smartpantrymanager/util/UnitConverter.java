package com.abrahams.smartpantrymanager.util;

import java.util.Locale;

public final class UnitConverter {

    private UnitConverter() {
    }

    public static String baseUnit(String unit) {
        switch (clean(unit)) {
            case "kg":
            case "1":
                return "g";

            case "ml":
            case "1":
            case "tsp":
                return "ml";

            case "pcs":
                return "pcs";

            default:
                return "";
        }
    }



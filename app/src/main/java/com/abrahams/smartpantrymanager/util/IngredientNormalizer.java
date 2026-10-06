package com.abrahams.smartpantrymanager.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class IngredientNormalizer {

    private IngredientNormalizer() {
    }

    private static final Map<String, String> PLURALS = new HashMap<>();

    static  {
        PLURALS.put()
        PLURALS.put()
        PLURALS.put()
        PLURALS.put()
        PLURALS.put()
        PLURALS.put()
        PLURALS.put()

    }

    public static  String normalize(String raw) {
        if (raw == null) {
            return "";
        }

        return raw.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "");
    }
}

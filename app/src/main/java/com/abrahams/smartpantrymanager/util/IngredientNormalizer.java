package com.abrahams.smartpantrymanager.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class IngredientNormalizer {

    // plural names that are common and the singular name that it changes to.
    private static final Map<String, String> PLURALS = new HashMap<>();

    static {
        PLURALS.put("tomatoes", "tomato");
        PLURALS.put("potatoes", "potato");
        PLURALS.put("eggs", "egg");
        PLURALS.put("mushrooms", "mushroom");
        PLURALS.put("peppers", "pepper");
        PLURALS.put("onions", "onion");
        PLURALS.put("carrots", "carrot");
        PLURALS.put("beans", "bean");
        PLURALS.put("cabbages", "cabbage");
        PLURALS.put("chillies", "chilli");
        PLURALS.put("berries", "berry");
        PLURALS.put("loaves", "loaf");
        PLURALS.put("leaves", "leaf");
    }

    private IngredientNormalizer() {
    }

    public static String normalize(String raw) {
        if (raw == null) {
            return "";
        }

        String cleaned = raw.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        int lastSpace = cleaned.lastIndexOf(' ');

        String lastWord = cleaned.substring(lastSpace + 1);
        String singular = PLURALS.get(lastWord);

        if (singular != null) {
            return cleaned.substring(0, lastSpace + 1) + singular;
        }
        return cleaned;
    }
}

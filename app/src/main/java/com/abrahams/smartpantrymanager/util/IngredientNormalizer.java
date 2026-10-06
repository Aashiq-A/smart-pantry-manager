package com.abrahams.smartpantrymanager.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class IngredientNormalizer {

    private IngredientNormalizer() {
    }

    private static final Map<String, String> PLURALS = new HashMap<>();

    static  {
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

    public static  String normalize(String raw) {
        if (raw == null) {
            return "";
        }

        String cleaned = raw.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");


        return raw.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "");
    }
}

package com.abrahams.smartpantrymanager.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class PantryPreferences {

    private static final String FILE_NAME = "pantry_preferences";
    private static final String SHOW_EXPIRY = "show_expiry_dates";

    private PantryPreferences() {
    }

    public static boolean shouldShowExpiryDates(Context context) {
        return preferences(context).getBoolean(SHOW_EXPIRY, true);
    }

    public static void setShowExpiryDates(
            Context context,
            boolean show) {

        preferences(context)
                .edit()
                .putBoolean(SHOW_EXPIRY, show)
                .apply();
    }

    private static SharedPreferences preferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(
                        FILE_NAME, Context.MODE_PRIVATE);
    }
}
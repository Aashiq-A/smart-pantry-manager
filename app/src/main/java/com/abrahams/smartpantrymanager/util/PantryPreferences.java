package com.abrahams.smartpantrymanager.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class PantryPreferences {

    private static final String FILE_NAME = "pantry_preferences";
    private static final String SHOW_EXPIRY = "show_expiry_dates";

    private PantryPreferences() {
    }

    // reads if the expiry dates must show.
    public static boolean shouldShowExpiryDates(Context context) {
        return preferences(context).getBoolean(SHOW_EXPIRY, true);
    }
    // saves the choice from the settings switch.
    public static void setShowExpiryDates(
            Context context,
            boolean show) {

        preferences(context)
                .edit()
                .putBoolean(SHOW_EXPIRY, show)
                .apply();
    }

    // opens the preferences file that is private to the app.
    private static SharedPreferences preferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(
                        FILE_NAME, Context.MODE_PRIVATE);
    }
}
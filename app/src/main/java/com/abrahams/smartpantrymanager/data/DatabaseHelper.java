package com.abrahams.smartpantrymanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// manage the apps local database.
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // this will store pantry items.
    public static final String Table_Pantry ="pantry_items";

    // items that are saved will have a unique ID.
    public static final String COL_PANTRY_ID = "id";

    // ingredient names.
    public static final String COL_PANTRY_NAME = "name";

    // available amount.
    public static final String COL_PANTRY_QTY = "quantity";

    // measurement that is used.
    public static final String COL_PANTRY_UNIT = "unit";

    // optional expiry date.
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // store the recipe names with cooking instructions.
    public static final String TABLE_RECIPES = "recipes";

    // each of the recipes will have a unique id.
    public static final String RECIPE_ID = "id";

    // name of the recipe.
    public static final String COL_NAME = "name";

    // the steps to prepare the recipe.
    public static final String COL_RECIPE_INSTRUCTIONS = "instructions";

    // sets the name and version of the database.
    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(),
                DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table_Pantry + " ("
                + COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PANTRY_NAME + " TEXT NOT NULL, "
                + COL_PANTRY_QTY + " REAL NOT NULL, "
                + COL_PANTRY_UNIT + " TEXT NOT NULL, "
                + COL_PANTRY_EXPIRY + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {

        throw  new IllegalStateException(
                "A database migration is required.");
    }
}



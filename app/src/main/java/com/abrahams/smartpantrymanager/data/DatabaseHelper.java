package com.abrahams.smartpantrymanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// imports added that saves pantry items.
import com.abrahams.smartpantrymanager.models.PantryItem;

// new imports added.
import com.abrahams.smartpantrymanager.models.Recipe;
import com.abrahams.smartpantrymanager.models.RecipeIngredient;

// new imports for pantry items.
import java.util.ArrayList;
import java.util.List;

// manage the apps local database.
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    // pantry tables with column names.
    public static final String Table_Pantry ="pantry_items";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QTY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // recipe table with column names.
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_INSTRUCTIONS = "instructions";

    // recipe ingredient table with column names.
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "ingredient_name";
    public static final String COL_RI_QTY = "quantity";
    public static final String COL_RI_UNIT = "unit";

    // sets the name and version of the database.
    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(),
                DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create pantry table.
        db.execSQL("CREATE TABLE " + Table_Pantry + " ("
                + COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PANTRY_NAME + " TEXT NOT NULL, "
                + COL_PANTRY_QTY + " REAL NOT NULL, "
                + COL_PANTRY_UNIT + " TEXT NOT NULL, "
                + COL_PANTRY_EXPIRY + " TEXT)");

        // creates recipe table.
        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " ("
                + COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPE_NAME + " TEXT NOT NULL, "
                + COL_RECIPE_INSTRUCTIONS + " TEXT NOT NULL)");

        // creates recipe ingredients table.
        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
                + COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RI_RECIPE_ID + " INTEGER NOT NULL, "
                + COL_RI_NAME + " TEXT NOT NULL, "
                + COL_RI_QTY + " REAL NOT NULL, "
                + COL_RI_UNIT + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + COL_RI_RECIPE_ID + ") REFERENCES "
                + TABLE_RECIPES + "(" + COL_RECIPE_ID + "))");

    }

    // Saves a pantry item.
    public long insertPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());

        return db.insert(Table_Pantry, null, values);
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                Table_Pantry,
                null,
                null,
                null,
                null,
                null,
                COL_PANTRY_NAME + " ASC")) {

            while (cursor.moveToNext()) {
                items.add(new PantryItem(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(COL_PANTRY_ID)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COL_PANTRY_NAME)),
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(COL_PANTRY_QTY)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY))
                ));
            }
        }

        return items;

    }

        // new method that reads one pantry using ID.
        public PantryItem getPantryItemById(int id) {
            SQLiteDatabase db = getReadableDatabase();

            try (Cursor cursor = db.query(
                    Table_Pantry,
                    null,
                    COL_PANTRY_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null)) {
                if (cursor.moveToFirst()) {
                    return new PantryItem(
                            cursor.getInt(
                                    cursor.getColumnIndexOrThrow(COL_PANTRY_ID)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(COL_PANTRY_NAME)),
                            cursor.getDouble(
                                    cursor.getColumnIndexOrThrow(COL_PANTRY_QTY)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(COL_PANTRY_UNIT)),
                            cursor.getString(
                                    cursor.getColumnIndexOrThrow(COL_PANTRY_EXPIRY))
                    );
                }
            }

            return null;
        }

    // new method created that saves to an existing item.
    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PANTRY_NAME, item.getName());
        values.put(COL_PANTRY_QTY, item.getQuantity());
        values.put(COL_PANTRY_UNIT, item.getUnit());
        values.put(COL_PANTRY_EXPIRY, item.getExpiryDate());

        return db.update(
                Table_Pantry,
                values,
                COL_PANTRY_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    // Deletes only the pantry item with this ID.
    public int deletePantryItem(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                Table_Pantry,
                COL_PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // method that reads all recipes that is sorted by name.
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COL_RECIPE_NAME + " ASC")) {

            while (cursor.moveToNext()) {
                Recipe recipe = new Recipe(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(COL_RECIPE_ID)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_RECIPE_INSTRUCTIONS))
                );

                loadIngredientsInto(db, recipe);
                recipes.add(recipe);
            }
        }

        return recipes;
    }

    // new method created.
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                COL_RECIPE_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null)) {

            if (cursor.moveToFirst()) {
                Recipe recipe = new Recipe(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(COL_RECIPE_ID)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COL_RECIPE_INSTRUCTIONS))
                );

                loadIngredientsInto(db, recipe);
                return recipe;
            }
        }

        return null;
    }

    // method helper that loads recipe ingredients.
    private void loadIngredientsInto(SQLiteDatabase db, Recipe recipe) {
        try (Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                COL_RI_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipe.getId())},
                null,
                null,
                null)) {

            while (cursor.moveToNext()) {
                recipe.addIngredient(new RecipeIngredient(
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(COL_RI_ID)),
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(COL_RI_RECIPE_ID)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COL_RI_NAME)),
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(COL_RI_QTY)),
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(COL_RI_UNIT))
                ));
            }
        }
    }

    // new method created that saves name and cooking steps.
    private void insertSeedRecipe(SQLiteDatabase db, String name,
                                  String instructions, String[][] ingredients) {

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_RECIPE_NAME, name);
        recipeValues.put(COL_RECIPE_INSTRUCTIONS, instructions);

        long recipeId = db.insertOrThrow(
                TABLE_RECIPES, null, recipeValues);

        for (String[] ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();

            ingredientValues.put(COL_RI_RECIPE_ID, recipeId);
            ingredientValues.put(COL_RI_NAME, ingredient[0]);
            ingredientValues.put(
                    COL_RI_QTY, Double.parseDouble(ingredient[1]));
            ingredientValues.put(COL_RI_UNIT, ingredient[2]);

            db.insertOrThrow(
                    TABLE_RECIPE_INGREDIENTS, null, ingredientValues);
        }
    }
    // simple recipes.
    private void seedRecipes(SQLiteDatabase db) {

        insertSeedRecipe(db, "Cheese Toast",
                "1. Butter the outside of the bread.\n"
                        + "2. Put the cheese between the slices.\n"
                        + "3. Cook in a pan until golden and the cheese melts.",
                new String[][]{
                        {"bread", "2", "pcs"},
                        {"cheese", "50", "g"},
                        {"butter", "10", "g"}
                });

        insertSeedRecipe(db, "Mushroom and Pepper Omelette",
                "1. Slice the mushrooms, chop the green pepper and beat the eggs.\n"
                        + "2. Melt the butter and cook the vegetables until soft.\n"
                        + "3. Add the eggs and cook until set.",
                new String[][]{
                        {"egg", "2", "pcs"},
                        {"mushroom", "50", "g"},
                        {"green pepper", "1", "pcs"},
                        {"butter", "10", "g"}
                });

        insertSeedRecipe(db, "Potato and Onion Hash",
                "1. Cut the potatoes and onion into small pieces.\n"
                        + "2. Heat the oil in a pan and add them.\n"
                        + "3. Cook, stirring, until the potatoes are soft and golden.",
                new String[][]{
                        {"potato", "3", "pcs"},
                        {"onion", "1", "pcs"},
                        {"oil", "15", "ml"}
                });
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        // Migration steps are needed before changing the version.
        throw new IllegalStateException(
                "A database migration is required.");
    }
}





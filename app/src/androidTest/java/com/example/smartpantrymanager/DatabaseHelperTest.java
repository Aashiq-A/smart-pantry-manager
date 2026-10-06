package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.abrahams.smartpantrymanager.data.DatabaseHelper;
import com.abrahams.smartpantrymanager.models.PantryItem;
import com.abrahams.smartpantrymanager.models.Recipe;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    @Test
    public void pantryTableHasExpectedColumns() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //  temporary database will be opened.
        try (DatabaseHelper helper = new DatabaseHelper(context); SQLiteDatabase database = SQLiteDatabase.create(null)) {

            // Creates the pantry table in the temporary database.
            helper.onCreate(database);

            // read the column names and not adding records.
            try (Cursor cursor = database.rawQuery("SELECT * FROM pantry_items LIMIT 0", null)) {

                String[] expectedColumns = {"id", "name", "quantity", "unit", "expiry_date"};

                // checking the column names and its order.
                assertArrayEquals(expectedColumns, cursor.getColumnNames());
            }
        }
    }

    // test method.
    @Test
    public void recipeIngredientRequiresExistingRecipe() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        try (DatabaseHelper helper = new DatabaseHelper(context); SQLiteDatabase database = SQLiteDatabase.create(null)) {

            helper.onConfigure(database);
            helper.onCreate(database);

            database.execSQL("INSERT INTO recipes (id, name, instructions) " + "VALUES (?, ?, ?)", new Object[]{1, "Test recipe", "Mix the ingredients"});

            database.execSQL("INSERT INTO recipe_ingredients " + "(recipe_id, ingredient_name, quantity, unit) " + "VALUES (?, ?, ?, ?)", new Object[]{1, "cheese", 50, "g"});

            try {

                database.execSQL("INSERT INTO recipe_ingredients " + "(recipe_id, ingredient_name, quantity, unit) " + "VALUES (?, ?, ?, ?)", new Object[]{999, "bread", 2, "pcs"});


                fail("An ingredient must link to an existing recipe");

            } catch (SQLiteConstraintException expected) {

            }
        }
    }

    @Test
    public void insertPantryItemSavesCorrectValues() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        try (SQLiteDatabase database = SQLiteDatabase.create(null)) {

            try (DatabaseHelper helper = new DatabaseHelper(context) {
                @Override
                public SQLiteDatabase getWritableDatabase() {
                    return database;
                }
            }) {

                helper.onConfigure(database);
                helper.onCreate(database);

                PantryItem item = new PantryItem("cheese", 50, "g", "2026-10-10");

                long newId = helper.insertPantryItem(item);

                assertTrue("Saving should return a valid ID", newId > 0);

                try (Cursor cursor = database.rawQuery("SELECT name, quantity, unit, expiry_date " + "FROM pantry_items WHERE id = ?", new String[]{String.valueOf(newId)})) {

                    assertTrue("The saved item should exist", cursor.moveToFirst());

                    assertEquals("cheese", cursor.getString(0));
                    assertEquals(50.0, cursor.getDouble(1), 0.001);
                    assertEquals("g", cursor.getString(2));
                    assertEquals("2026-10-10", cursor.getString(3));

                    assertTrue("Only one matching record should exist", !cursor.moveToNext());
                }
            }
        }
    }

    // new test method created.
    @Test
    public void getAllPantryItemsReturnsSavedItems() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        try (SQLiteDatabase database = SQLiteDatabase.create(null)) {

            try (DatabaseHelper helper = new DatabaseHelper(context) {
                @Override
                public SQLiteDatabase getWritableDatabase() {
                    return database;
                }

                @Override
                public SQLiteDatabase getReadableDatabase() {
                    return database;
                }
            }) {

                helper.onConfigure(database);
                helper.onCreate(database);

                assertTrue(helper.getAllPantryItems().isEmpty());

                long riceId = helper.insertPantryItem(new PantryItem("rice", 200, "g", null));

                long cheeseId = helper.insertPantryItem(new PantryItem("cheese", 50, "g", "2026-10-10"));

                List<PantryItem> items = helper.getAllPantryItems();


                assertEquals(2, items.size());

                PantryItem first = items.get(0);
                assertEquals(cheeseId, (long) first.getId());
                assertEquals("cheese", first.getName());
                assertEquals(50.0, first.getQuantity(), 0.001);
                assertEquals("g", first.getUnit());
                assertEquals("2026-10-10", first.getExpiryDate());

                PantryItem second = items.get(1);
                assertEquals(riceId, (long) second.getId());
                assertEquals("rice", second.getName());
                assertEquals(200.0, second.getQuantity(), 0.001);
                assertEquals("g", second.getUnit());


                assertTrue(second.getExpiryDate() == null);
            }
        }
    }

    // created new test.
    @Test
    public void updatePantryItemChangesOnlySelectedItem() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Uses a temporary database for this test.
        try (SQLiteDatabase database = SQLiteDatabase.create(null)) {

            try (DatabaseHelper helper = new DatabaseHelper(context) {
                @Override
                public SQLiteDatabase getWritableDatabase() {
                    return database;
                }

                @Override
                public SQLiteDatabase getReadableDatabase() {
                    return database;
                }
            }) {

                helper.onConfigure(database);
                helper.onCreate(database);

                long cheeseId = helper.insertPantryItem(new PantryItem("cheese", 50, "g", "2026-10-10"));


                long riceId = helper.insertPantryItem(new PantryItem("rice", 200, "g", null));

                assertTrue(cheeseId > 0);
                assertTrue(riceId > 0);

                PantryItem editedItem = new PantryItem((int) cheeseId, "cheddar cheese", 0.1, "kg", "2026-10-15");

                int changedRecords = helper.updatePantryItem(editedItem);

                assertEquals(1, changedRecords);

                List<PantryItem> items = helper.getAllPantryItems();
                assertEquals(2, items.size());

                PantryItem first = items.get(0);
                assertEquals(cheeseId, (long) first.getId());
                assertEquals("cheddar cheese", first.getName());
                assertEquals(0.1, first.getQuantity(), 0.001);
                assertEquals("kg", first.getUnit());
                assertEquals("2026-10-15", first.getExpiryDate());

                PantryItem second = items.get(1);
                assertEquals(riceId, (long) second.getId());
                assertEquals("rice", second.getName());
                assertEquals(200.0, second.getQuantity(), 0.001);
                assertEquals("g", second.getUnit());
                assertTrue(second.getExpiryDate() == null);
            }
        }
    }

    @Test
    public void deletePantryItemRemovesOnlySelectedItem() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();


        try (SQLiteDatabase database = SQLiteDatabase.create(null)) {

            try (DatabaseHelper helper = new DatabaseHelper(context) {
                @Override
                public SQLiteDatabase getWritableDatabase() {
                    return database;
                }

                @Override
                public SQLiteDatabase getReadableDatabase() {
                    return database;
                }
            }) {

                helper.onConfigure(database);
                helper.onCreate(database);

                long cheeseId = helper.insertPantryItem(new PantryItem("cheese", 50, "g", null));

                long riceId = helper.insertPantryItem(new PantryItem("rice", 200, "g", null));

                assertTrue(cheeseId > 0);
                assertTrue(riceId > 0);

                int deletedRecords = helper.deletePantryItem((int) cheeseId);
                assertEquals(1, deletedRecords);

                List<PantryItem> items = helper.getAllPantryItems();
                assertEquals(1, items.size());

                PantryItem remaining = items.get(0);
                assertEquals(riceId, (long) remaining.getId());
                assertEquals("rice", remaining.getName());
                assertEquals(200.0, remaining.getQuantity(), 0.001);
                assertEquals("g", remaining.getUnit());
                assertTrue(remaining.getExpiryDate() == null);

                assertEquals(0, helper.deletePantryItem((int) cheeseId));

                assertEquals(1, helper.getAllPantryItems().size());
            }
        }
    }

    // new test created to find item by ID.
    @Test
    public void getPantryItemByIdFindsCorrectItem() {
        Context context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();

        try (SQLiteDatabase database = SQLiteDatabase.create(null);
             DatabaseHelper helper = new DatabaseHelper(context) {
                 @Override
                 public SQLiteDatabase getReadableDatabase() {
                     return database;
                 }
             }) {
            helper.onConfigure(database);
            helper.onCreate(database);

            database.execSQL(
                    "INSERT INTO pantry_items "
                            + "(id, name, quantity, unit) VALUES (?, ?, ?, ?)",
                    new Object[]{1, "cheese", 50, "g"});

            database.execSQL(
                    "INSERT INTO pantry_items "
                            + "(id, name, quantity, unit) VALUES (?, ?, ?, ?)",
                    new Object[]{2, "rice", 200, "g"});

            PantryItem item = helper.getPantryItemById(2);

            assertTrue("The item should exist", item != null);
            assertEquals(2, item.getId());
            assertEquals("rice", item.getName());
            assertEquals(200.0, item.getQuantity(), 0.001);
            assertEquals("g", item.getUnit());
            assertNull(item.getExpiryDate());

            assertNull(helper.getPantryItemById(999));
        }
    }

    // new test created.
    @Test
    public void getAllRecipesLoadsMatchingIngredients() {
        Context context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();

        try (SQLiteDatabase database = SQLiteDatabase.create(null);
             DatabaseHelper helper = new DatabaseHelper(context) {
                 @Override
                 public SQLiteDatabase getReadableDatabase() {
                     return database;
                 }
             }) {

            helper.onConfigure(database);
            helper.onCreate(database);

            database.execSQL(
                    "INSERT INTO recipes (id, name, instructions) VALUES (?, ?, ?)",
                    new Object[]{1, "Toast", "Toast the bread"});

            database.execSQL(
                    "INSERT INTO recipes (id, name, instructions) VALUES (?, ?, ?)",
                    new Object[]{2, "Rice", "Cook the rice"});

            database.execSQL(
                    "INSERT INTO recipe_ingredients "
                            + "(recipe_id, ingredient_name, quantity, unit) "
                            + "VALUES (?, ?, ?, ?)",
                    new Object[]{1, "bread", 2, "pcs"});

            database.execSQL(
                    "INSERT INTO recipe_ingredients "
                            + "(recipe_id, ingredient_name, quantity, unit) "
                            + "VALUES (?, ?, ?, ?)",
                    new Object[]{2, "rice", 200, "g"});

            List<Recipe> recipes = helper.getAllRecipes();
            assertEquals(2, recipes.size());

            Recipe first = recipes.get(0);
            assertEquals(2, first.getId());
            assertEquals("Rice", first.getName());
            assertEquals("Cook the rice", first.getInstructions());
            assertEquals(1, first.getIngredients().size());
            assertEquals(2, first.getIngredients().get(0).getRecipeId());
            assertEquals("rice", first.getIngredients().get(0).getName());
            assertEquals(200.0,
                    first.getIngredients().get(0).getQuantity(), 0.001);
            assertEquals("g", first.getIngredients().get(0).getUnit());

            Recipe second = recipes.get(1);
            assertEquals("Toast", second.getName());
            assertEquals(1, second.getIngredients().size());
            assertEquals(1, second.getIngredients().get(0).getRecipeId());
            assertEquals("bread", second.getIngredients().get(0).getName());
            assertEquals(2.0,
                    second.getIngredients().get(0).getQuantity(), 0.001);
            assertEquals("pcs", second.getIngredients().get(0).getUnit());
        }
    }
}











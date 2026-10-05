package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.abrahams.smartpantrymanager.data.DatabaseHelper;
import com.abrahams.smartpantrymanager.models.PantryItem;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    @Test
    public void pantryTableHasExpectedColumns() {
        Context context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();

        //  temporary database will be opened.
        try (DatabaseHelper helper = new DatabaseHelper(context);
             SQLiteDatabase database = SQLiteDatabase.create(null)) {

            // Creates the pantry table in the temporary database.
            helper.onCreate(database);

            // read the column names and not adding records.
            try (Cursor cursor = database.rawQuery(
                    "SELECT * FROM pantry_items LIMIT 0", null)) {

                String[] expectedColumns = {
                        "id",
                        "name",
                        "quantity",
                        "unit",
                        "expiry_date"
                };

                // checking the column names and its order.
                assertArrayEquals(
                        expectedColumns, cursor.getColumnNames());
            }
        }
    }

    // test method.
    @Test
    public void recipeIngredientRequiresExistingRecipe() {
        Context context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();

        try (DatabaseHelper helper = new DatabaseHelper(context);
             SQLiteDatabase database = SQLiteDatabase.create(null)) {

            helper.onConfigure(database);
            helper.onCreate(database);

            database.execSQL(
                    "INSERT INTO recipes (id, name, instructions) "
                            + "VALUES (?, ?, ?)",
                    new Object[]{
                            1,
                            "Test recipe",
                            "Mix the ingredients"
                    });

            database.execSQL(
                    "INSERT INTO recipe_ingredients "
                            + "(recipe_id, ingredient_name, quantity, unit) "
                            + "VALUES (?, ?, ?, ?)",
                    new Object[]{1, "cheese", 50, "g"});

            try {

                database.execSQL(
                        "INSERT INTO recipe_ingredients "
                                + "(recipe_id, ingredient_name, quantity, unit) "
                                + "VALUES (?, ?, ?, ?)",
                        new Object[]{999, "bread", 2, "pcs"});


                fail("An ingredient must link to an existing recipe");

            } catch (SQLiteConstraintException expected) {

            }
        }
    }

    @Test
    public void insertPantryItemSavesCorrectValues() {
        Context context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();

        try (SQLiteDatabase database = SQLiteDatabase.create(null)) {

            try (DatabaseHelper helper = new DatabaseHelper(context) {
                @Override
                public SQLiteDatabase getWritableDatabase() {
                    return database;
                }
            }) {

                helper.onConfigure(database);
                helper.onCreate(database);

                PantryItem item = new PantryItem(
                        "cheese", 50, "g", "2026-10-10");

                long newId = helper.insertPantryItem(item);

                assertTrue(
                        "Saving should return a valid ID",
                        newId > 0);

                try (Cursor cursor = database.rawQuery(
                        "SELECT name, quantity, unit, expiry_date "
                                + "FROM pantry_items WHERE id = ?",
                        new String[]{String.valueOf(newId)})) {

                    assertTrue(
                            "The saved item should exist",
                            cursor.moveToFirst());

                    assertEquals("cheese", cursor.getString(0));
                    assertEquals(50.0, cursor.getDouble(1), 0.001);
                    assertEquals("g", cursor.getString(2));
                    assertEquals("2026-10-10", cursor.getString(3));

                    assertTrue(
                            "Only one matching record should exist",
                            !cursor.moveToNext());
                }
            }
        }
    }

    // new test created
    @Test
    public void getAllPantryItemsReturnsSavedItems() {
        Context context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();

        try (SQLiteDatabase database = SQLiteDatabase.create(null)) {

            try (DatabaseHelper helper = new DatabaseHelper(context) {
                @Override
                public SQLiteDatabase getWritableDatabase() {
                    return database;
                }



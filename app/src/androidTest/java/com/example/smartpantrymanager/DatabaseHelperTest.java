package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.abrahams.smartpantrymanager.data.DatabaseHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;
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
}

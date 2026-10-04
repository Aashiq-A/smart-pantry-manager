package com.example.smartpantrymanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.abrahams.smartpantrymanager.data.DatabaseHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;

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
}
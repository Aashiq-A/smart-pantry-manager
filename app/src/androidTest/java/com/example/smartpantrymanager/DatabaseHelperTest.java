package com.example.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.abrahams.smartpantrymanager.data.DatabaseHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

        @RunWith(AndroidJUnit4.class)
        public class DatabaseHelperTest {

            @Test
            public void temporaryDatabaseOpens() {
                Context context = InstrumentationRegistry
                        .getInstrumentation().getTargetContext();

                // this will open temporary database that is in memory.
                try (DatabaseHelper helper = new DatabaseHelper(context);
                     SQLiteDatabase database = SQLiteDatabase.create(null)) {


                }
            }
        }
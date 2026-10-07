package com.example.smartpantrymanager;

import android.os.Bundle;

import com.abrahams.smartpantrymanager.ui.SettingsFragment;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.abrahams.smartpantrymanager.ui.PantryFragment;
import com.abrahams.smartpantrymanager.ui.RecipesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        BottomNavigationView navigation =
                findViewById(R.id.bottomNavigation);

        navigation.setOnItemSelectedListener(item -> {
            Fragment current = getSupportFragmentManager()
                    .findFragmentById(R.id.fragmentContainer);

            Fragment destination;

            if (item.getItemId() == R.id.navPantry) {
                if (current instanceof PantryFragment) {
                    return true;
                }

                destination = new PantryFragment();

            } else if (item.getItemId() == R.id.navRecipes) {
                if (current instanceof RecipesFragment) {
                    return true;
                }

                destination = new RecipesFragment();

            } else if (item.getItemId() == R.id.navSettings) {
                if (current instanceof SettingsFragment) {
                    return true;
                }

                destination = new SettingsFragment();

            } else {
                return false;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, destination)
                    .commit();

            return true;
        });

        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navPantry);
        }
    }
}
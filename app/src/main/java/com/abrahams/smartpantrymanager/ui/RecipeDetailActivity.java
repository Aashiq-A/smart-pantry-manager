package com.abrahams.smartpantrymanager.ui;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.abrahams.smartpantrymanager.data.DatabaseHelper;
import com.abrahams.smartpantrymanager.models.Recipe;
import com.abrahams.smartpantrymanager.models.RecipeIngredient;
import com.example.smartpantrymanager.R;

import java.math.BigDecimal;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    // PART 1: Set up the screen and back button.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        View root = findViewById(R.id.recipeDetailRoot);

        ViewCompat.setOnApplyWindowInsetsListener(
                root, (view, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });

        findViewById(R.id.buttonBackToRecipes)
                .setOnClickListener(view -> finish());

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        loadRecipe(recipeId);
    } // onCreate ends here.

    // PART 2: Load the recipe, ingredients and cooking steps.

    private void loadRecipe(int recipeId) {
        try (DatabaseHelper helper = new DatabaseHelper(this)) {
            Recipe recipe = helper.getRecipeById(recipeId);

            if (recipe == null) {
                showLoadError();
                return;
            }

            TextView textName = findViewById(
                    R.id.textDetailRecipeName);

            TextView textIngredients = findViewById(
                    R.id.textDetailIngredients);

            TextView textInstructions = findViewById(
                    R.id.textDetailInstructions);

            textName.setText(recipe.getName());
            textInstructions.setText(recipe.getInstructions());

            StringBuilder ingredients = new StringBuilder();

            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                if (ingredients.length() > 0) {
                    ingredients.append("\n");
                }

                String quantity = BigDecimal.valueOf(
                                ingredient.getQuantity())
                        .stripTrailingZeros()
                        .toPlainString();

                ingredients.append("• ")
                        .append(ingredient.getName())
                        .append(" — ")
                        .append(quantity)
                        .append(" ")
                        .append(ingredient.getUnit());
            }

            textIngredients.setText(ingredients.toString());

        } catch (SQLiteException exception) {
            showLoadError();
        }
    }

    // PART 3: Handle a missing recipe or database error.

    private void showLoadError() {
        Toast.makeText(
                this,
                R.string.could_not_load_recipe,
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
# Smart Pantry Manager

Smart Pantry Manager is a Java Android application where it will help users to reduce any food waste. Users can add ingredients that they have at home, and the application will help them suggests recipes they are able to make using only the ingredients that they have. A recipe it will only show when every ingredient is in the pantry in the required amount. This is then called **strict matching**.

## Features

- Add, edit and delete pantry items with a name, quantity, unit and optional expiry date.
- 18 recipes are loaded into the database the first time the app opens.
- **Suggested Recipes** It will only show recipes when every ingredient it is in the pantry in the required amount or more.
- Different units it will automatically be converted (kg to g, l to ml, 1 tsp = 5 ml), and plural names such as "tomatoes" still match "tomato".
- Recipe details it will show the ingredient list and steps for cooking.
- A Settings screen this will allow the user to show or  even hide expiry dates, and the choice it is then saved by SharedPreferences.
- Input validation, plus clear messages when the pantry or recipe list is empty.

## Database choice: SQLite

I chose SQLite (via `SQLiteOpenHelper`) for three reasons:
1. **Local, single-user data.** The data belongs to one user on one phone, so there's no need for a server, an account or internet access, and the app works offline.
2. **Relational data.** Each recipe has many ingredients linked by a foreign key, which suits SQL tables well.
3. **Built into Android.** SQLite comes with Android, and it is the database approach covered in the module.

The app uses three tables: `pantry_items`, `recipes` and `recipe_ingredients` (see `docs/sqlitedatabase.png`).

## Setup and run

1. Install the latest stable version of Android Studio.
2. Clone the repository:
```bash
   git clone https://github.com/Aashiq-A/smart-pantry-manager.git
```
3. Open the project folder in Android Studio and wait for the Gradle sync to finish.
4. Connect an Android phone running Android 7.0 (API 24) or newer with USB debugging enabled, or start an emulator.
5. Click **Run**. The database and recipes are created the first time the app opens.

## Tests

- **Unit tests** (matching, unit conversion and name cleaning): right-click `app/src/test/java` > **Run 'Tests in java'**
- **Database tests** (need a phone or emulator): right-click `app/src/androidTest/java` > **Run 'Tests in java'**


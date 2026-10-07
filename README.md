Smart Pantry Manager
Smart Pantry Manager it is a Java Android application where it helps on cutting down food waste. 
The user then will add the ingredients they have at home in their pantry and then the application will suggest recipes that they can make right now using only the ingredients that is leftover. 
A recipe it will only show if every single ingredient is in the pantry in the amount that is needed and this is called strict matching.

Features
Add, edit and delete pantry items that has a name, quantity, unit and an expiry date that is optional.
18 recipes that is loaded inside of the database the very first time the app opens.
Suggested Recipes this only shows the recipes where every ingredient is in the pantry in the amount that is needed or more.
Units that are different it gets converted, kg to g, l to ml and 1 tsp is 5 ml, and names that are plural like tomatoes it will still match tomato.
Recipe details that shows the list of ingredients and the steps for cooking.
Settings where the expiry dates can be shown or hidden and the choice it is saved with SharedPreferences.
Input validation and messages that are clear when the pantry or the recipe list is empty.

Database choice SQLite
I chose SQLite using `SQLiteOpenHelper` for three reasons.
Firstly, the data it belongs to one user on one phone so there is no need for a server, an account or internet and the app it is able to work offline.
Secondly, the data is relational because one recipe it has many ingredients that is linked with a foreign key and this suits tables in SQL very well.
Thirdly, SQLite it is built inside of Android and it is the database approach that was covered in the module.
The tables that is used are `pantry_items`, `recipes` and `recipe_ingredients` (see `docs/sqlitedatabase.png`).

Setup and run
Install Android Studio, the latest version that is stable.
Clone the repository:`git clone https://github.com/Aashiq-A/smart-pantry-manager.git`
Open the project folder in Android Studio and wait for Gradle to finish the sync.
Connect an Android phone that is Android 7.0 (API 24) or newer with USB debugging that is turned on, or start an emulator.
Click Run and the database and the recipes it gets created the first time the app opens.

Tests
Unit tests that checks the matching, unit conversion and the cleaning of names: right-click `app/src/test/java` > Run 'Tests in java'
Database tests that needs a phone or an emulator: right-click `app/src/androidTest/java` > Run 'Tests in java'


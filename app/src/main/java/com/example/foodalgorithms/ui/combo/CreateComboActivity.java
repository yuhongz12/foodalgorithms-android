package com.example.foodalgorithms.ui.combo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;
import com.example.foodalgorithms.ui.search.cocktail.ResultCocktailItem;
import com.example.foodalgorithms.ui.search.cocktail.SearchCocktailFragment;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class CreateComboActivity extends AppCompatActivity {
    // Initialize variable
    TextView recipeSelectSpinner;
    TextView cocktailSelectSpinner;
    List<ResultFoodItem> foodSearchList;
    List<ResultCocktailItem> cocktailItemList;
    ArrayAdapter<String> foodItemArrayAdapter;
    Dialog dialog;

    ComboViewModel comboViewModel;

    List<String> stringList;

    EditText createComboName;
    EditText createComboDescription;

    Button createComboSaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_combo);

        comboViewModel = new ViewModelProvider(this).get(ComboViewModel.class);


        recipeSelectSpinner = findViewById(R.id.CreateComboRecipeSelect);
        cocktailSelectSpinner = findViewById(R.id.CreateComboCocktailSelect);
        foodSearchList = new ArrayList<>();
        cocktailItemList = new ArrayList<>();
        stringList = new ArrayList<>();
        createComboName = findViewById(R.id.CreateComboNameInput);
        createComboDescription = findViewById(R.id.CreateComboDescriptionInput);
        createComboSaveButton = findViewById(R.id.CreateComboSaveButton);

        recipeSelectSpinner.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog=new Dialog(CreateComboActivity.this);
                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels*0.9), (int) (getResources().getDisplayMetrics().widthPixels*0.9));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // Initialize and assign variable
                EditText spinnerSearchBar = dialog.findViewById(R.id.SpinnerSearchBar);
                ListView spinnerListView = dialog.findViewById(R.id.SpinnerListView);
                Button spinnerButton = dialog.findViewById(R.id.SpinnerSearchButton);

            foodItemArrayAdapter =  new ArrayAdapter<>(CreateComboActivity.this, android.R.layout.simple_list_item_1, stringList);
            spinnerListView.setAdapter(foodItemArrayAdapter);
            //search button inside spinner
            spinnerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        foodSearchList.clear();
                        stringList.clear();
                        foodItemArrayAdapter.notifyDataSetChanged();
                    String searchTerm = spinnerSearchBar.getText().toString();
                    String foodURL = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + searchTerm;
                    new CreateComboActivity.SearchFoodTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, foodURL);
                    Log.i("CREATE COMBO SIZE clicked", stringList.size() + "");
                }
            });
            spinnerButton.performClick();
            // select item and close spinner when item clicked
            spinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    recipeSelectSpinner.setText(foodItemArrayAdapter.getItem(position));
                    comboViewModel.getFoodItem().setValue(foodSearchList.get(position));
                    dialog.dismiss();

                }
            });
            }
        });

        cocktailSelectSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog=new Dialog(CreateComboActivity.this);
                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels*0.9), (int) (getResources().getDisplayMetrics().widthPixels*0.9));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // Initialize and assign variable
                EditText spinnerSearchBar = dialog.findViewById(R.id.SpinnerSearchBar);
                ListView spinnerListView = dialog.findViewById(R.id.SpinnerListView);
                Button spinnerButton = dialog.findViewById(R.id.SpinnerSearchButton);


                //foodItemArrayAdapter = new ArrayAdapter<>( , android.R.layout.simple_list_item_1, stringList);
                foodItemArrayAdapter =  new ArrayAdapter<>(CreateComboActivity.this, android.R.layout.simple_list_item_1, stringList);
                // set adapter
                spinnerListView.setAdapter(foodItemArrayAdapter);
                spinnerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cocktailItemList.clear();
                        stringList.clear();
                        foodItemArrayAdapter.notifyDataSetChanged();
                        String searchTerm = spinnerSearchBar.getText().toString();
                        String foodURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + searchTerm;
                        new SearchCocktailTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, foodURL);
                    }
                });
                spinnerButton.performClick();
                spinnerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        cocktailSelectSpinner.setText(foodItemArrayAdapter.getItem(position));
                        comboViewModel.getCocktailItem().setValue(cocktailItemList.get(position));
                        // Dismiss dialog
                        dialog.dismiss();

                    }
                });
            }
        });

        createComboSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comboName = createComboName.getText().toString();
                String comboDescription = createComboDescription.getText().toString();

                if (!comboName.isEmpty() && !comboDescription.isEmpty()) {
                    ResultFoodItem food = comboViewModel.getFoodItem().getValue();
                    ResultCocktailItem cocktail = comboViewModel.getCocktailItem().getValue();
                    if ((food != null && food.getIdMeal() >= 0) && (cocktail != null && cocktail.getIdDrink() >= 0)) {
                        Toast.makeText(CreateComboActivity.this, "Combo created", Toast.LENGTH_SHORT).show();
                        Log.i("combo created", comboName + "; " + comboDescription + ", " + food.getStrMeal() + ", " + cocktail.getStrDrink());
                        finish();
                        DatabaseReference comboRef = FirebaseDatabase.getInstance().getReference().child("combos");
                        String newComboKey = comboRef.push().getKey();
                        Combo newCombo = new Combo(newComboKey, comboName, comboDescription, food, cocktail);
                        comboRef.child(newComboKey).setValue(newCombo);
                    } else {
                        Toast.makeText(CreateComboActivity.this, "Please select a food and cocktail for the combo!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateComboActivity.this, "Please enter a combo name and description!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class SearchFoodTask extends AsyncTask<String, Void, String> {

        String jsonData;

        @Override
        protected String doInBackground(String... urls) {
            jsonData = downloadFromURL(urls[0]);
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                //    if (cocktailOrFood.equals("food")) {
                Log.i("JSON FOOD", s);
                JSONArray arr = jsonObject.getJSONArray("meals");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject meal = arr.getJSONObject(i);
                    int idMeal = meal.getInt("idMeal");
                    String strMeal = meal.getString("strMeal");
                    String strMealThumb = meal.getString("strMealThumb");
                    ResultFoodItem resultFoodItem = new ResultFoodItem(idMeal, strMeal, strMealThumb);
                    foodSearchList.add(resultFoodItem);

                }
                stringList.addAll(foodSearchList.stream().map(ResultFoodItem::getStrMeal).collect(Collectors.toList()));
                Log.i("CREATE COMBO SIZE list", stringList.size() + "");
                foodItemArrayAdapter.notifyDataSetChanged();
                Log.i("CREATE COMBO SIZE adapter", foodItemArrayAdapter.getCount() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String downloadFromURL(String url) {
            InputStream is = null;
            StringBuffer result = new StringBuffer();
            try {
                URL myURL = new URL(url);
                HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result.toString();
        }
    }

    class SearchCocktailTask extends AsyncTask<String, Void, String> {
        String jsonData;
        @Override
        protected String doInBackground(String... urls) {
            jsonData = downloadFromURL(urls[0]);
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                //    if (cocktailOrFood.equals("food")) {
                Log.i("JSON FOOD", s);
                JSONArray arr = jsonObject.getJSONArray("drinks");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject meal = arr.getJSONObject(i);
                    int idDrink = meal.getInt("idDrink");
                    String strDrink = meal.getString("strDrink");
                    String strDrinkThumb = meal.getString("strDrinkThumb");
                    ResultCocktailItem resultCocktailItem = new ResultCocktailItem(idDrink, strDrink, strDrinkThumb);
                    cocktailItemList.add(resultCocktailItem);
                }
                stringList.addAll(cocktailItemList.stream().map(ResultCocktailItem::getStrDrink).collect(Collectors.toList()));
                Log.i("CREATE COMBO SIZE list", stringList.size() + "");
                foodItemArrayAdapter.notifyDataSetChanged();
                Log.i("CREATE COMBO SIZE adapter", foodItemArrayAdapter.getCount() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String downloadFromURL(String url) {
            InputStream is = null;
            StringBuffer result = new StringBuffer();
            try {
                URL myURL = new URL(url);
                HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result.toString();
        }


    }
}
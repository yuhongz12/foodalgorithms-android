package com.example.foodalgorithms.ui.combo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.search.SearchViewModel;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;

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

import javax.net.ssl.HttpsURLConnection;

public class CreateComboActivity extends AppCompatActivity {
    // Initialize variable
    TextView textview;
    ArrayList<ResultFoodItem> foodSearchList;
    ArrayAdapter<ResultFoodItem> foodItemArrayAdapter;
    Dialog dialog;

    ComboViewModel comboViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_combo);

        comboViewModel = new ViewModelProvider(this).get(ComboViewModel.class);


        textview = findViewById(R.id.testView);
        foodSearchList =new ArrayList<>();


        textview.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog=new Dialog(CreateComboActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout((int) (getResources().getDisplayMetrics().widthPixels*0.9), (int) (getResources().getDisplayMetrics().widthPixels*0.9));

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);
                Button spinnerButton = dialog.findViewById(R.id.SpinnerSearchButton);

                // Initialize array adapter
        foodItemArrayAdapter = new ArrayAdapter<>(CreateComboActivity.this, android.R.layout.simple_gallery_item, foodSearchList);

                // set adapter
                listView.setAdapter(foodItemArrayAdapter);
            spinnerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        foodSearchList.clear();
                        foodItemArrayAdapter.notifyDataSetChanged();
                    String searchTerm = editText.getText().toString();
                    String foodURL = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + searchTerm;
                    new CreateComboActivity.SearchFoodTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, foodURL);
                }
            });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        textview.setText(foodItemArrayAdapter.getItem(position).getStrMeal());
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
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
                foodItemArrayAdapter.notifyDataSetChanged();
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
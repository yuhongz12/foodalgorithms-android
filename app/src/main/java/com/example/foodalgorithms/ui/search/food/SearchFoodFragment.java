package com.example.foodalgorithms.ui.search.food;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.search.SearchViewModel;

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

import javax.net.ssl.HttpsURLConnection;

public class SearchFoodFragment extends Fragment {

    List<ResultFoodItem> foodList = new ArrayList<>();

    EditText searchFoodInput;
    Button searchFoodButton;
    RecyclerView searchFoodRecylerView;

    SearchFoodAdapter searchFoodAdapter;
    SearchViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_food, container, false);
        searchFoodInput = view.findViewById(R.id.SearchFoodInput);
        searchFoodButton = view.findViewById(R.id.SearchFoodButton);
        searchFoodRecylerView = view.findViewById(R.id.SearchCocktailRecyclerView);
        searchFoodAdapter = new SearchFoodAdapter(getContext(), foodList);
        searchFoodRecylerView.setAdapter(searchFoodAdapter);
        searchFoodRecylerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        searchFoodInput.setText(viewModel.getFoodSearchText().getValue());
        searchFoodInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.getFoodSearchText().setValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        searchFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = searchFoodInput.getText().toString();
                // run background service to fetch api search result
                foodList.clear();
                searchFoodAdapter.notifyDataSetChanged();
                String foodURL = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + searchTerm;
                new SearchFoodTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, foodURL);
                searchFoodInput.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        searchFoodButton.performClick();

        return view;
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
                    foodList.add(resultFoodItem);
                }
                searchFoodAdapter.notifyDataSetChanged();
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
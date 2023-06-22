package com.example.foodalgorithms.ui.search.cocktail;

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


public class SearchCocktailFragment
        extends Fragment {

    List<ResultCocktailItem> cocktailItemList = new ArrayList<>();

    EditText searchCocktailInput;
    Button searchCocktailButton;
    RecyclerView searchCocktailRecyclerView;

    SearchCocktailAdapter cocktailAdapter;

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
        View view = inflater.inflate(R.layout.fragment_search_cocktail, container, false);

        searchCocktailInput = view.findViewById(R.id.SearchCocktailInput);
        searchCocktailButton = view.findViewById(R.id.SearchCocktailButton);
        searchCocktailRecyclerView = view.findViewById(R.id.SearchCocktailRecyclerView);
        cocktailAdapter = new SearchCocktailAdapter(getContext(), cocktailItemList);
        searchCocktailRecyclerView.setAdapter(cocktailAdapter);
        searchCocktailRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        searchCocktailInput.setText(viewModel.getCocktailSearchText().getValue());
        searchCocktailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.getCocktailSearchText().setValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchCocktailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = searchCocktailInput.getText().toString();
                cocktailItemList.clear();
                cocktailAdapter.notifyDataSetChanged();
                String foodURL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + searchTerm;
                new SearchCocktailTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, foodURL);
                searchCocktailInput.clearFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        searchCocktailButton.performClick();

        return view;
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
                cocktailAdapter.notifyDataSetChanged();
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
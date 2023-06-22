package com.example.foodalgorithms.ui.cocktail;

import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodalgorithms.DownloadImageTask;
import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;

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

import javax.net.ssl.HttpsURLConnection;

public class CocktailDetailFragment extends Fragment {

    private CocktailDetailViewModel mViewModel;

    TextView cocktailDetailName;
    TextView cocktailDetailCategory;
    TextView cocktailDetailAlcoholic;
    TextView cocktailDetailIngredientsText;
    TextView cocktailDetailDirectionsText;
    ImageView cocktailDetailImage;

    TextView cocktailDetailGlass;

    int idDrink;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idDrink = bundle.getInt("idDrink");
            Log.i("Cocktail DETAILS ID cocktail", idDrink + "");
        }
        View view = inflater.inflate(R.layout.fragment_cocktail_detail, container, false);
         cocktailDetailName = view.findViewById(R.id.CocktailDetailName);
         cocktailDetailCategory = view.findViewById(R.id.CocktailDetailCategory);
         cocktailDetailAlcoholic = view.findViewById(R.id.CocktailDetailAlcoholic);
         cocktailDetailIngredientsText = view.findViewById(R.id.CocktailDetailIngredientsText);
         cocktailDetailDirectionsText = view.findViewById(R.id.CocktailDetailInstructionText);
         cocktailDetailImage = view.findViewById(R.id.CocktailDetailImage);
         cocktailDetailGlass = view.findViewById(R.id.CocktailDetailGlass);
        String foodURL = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + idDrink;
        new DownloadCocktailRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,foodURL);
        return view;
    }

private class DownloadCocktailRecipe extends AsyncTask<String, Void, String> {
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
            JSONObject drink = jsonObject.getJSONArray("drinks").getJSONObject(0);
            int idDrink = drink.getInt("idDrink");
            String strDrink = drink.getString("strDrink");
            String strCategory = drink.getString("strCategory");
            String strAlcoholic = drink.getString("strAlcoholic");
            String strDrinkThumb = drink.getString("strDrinkThumb");
            String strGlass = drink.getString("strGlass");
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (int i = 1; i <= 20; i++) {

                String strIngredients = drink.getString("strIngredient" + i);
                String strMeasure = drink.getString("strMeasure" + i);
                if (strIngredients.equals("null") || strIngredients.isEmpty()) {
                    break;
                } else {
                    ingredientsBuilder.append(i).append(". ");
                    if (!strMeasure.equals("null")) {
                        ingredientsBuilder.append(strMeasure);
                    }
                    ingredientsBuilder.append(" ").append(strIngredients).append("\n");
                }
            }
            String strInstructions = drink.getString("strInstructions");
            cocktailDetailName.setText(strDrink);
            cocktailDetailCategory.setText(strCategory);
            cocktailDetailAlcoholic.setText(strAlcoholic);
            cocktailDetailIngredientsText.setText(ingredientsBuilder.toString());
            cocktailDetailDirectionsText.setText(strInstructions);
            cocktailDetailGlass.setText("Serve in a " + strGlass);
            new DownloadImageTask(cocktailDetailImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strDrinkThumb);

//                } else if (cocktailOrFood.equals("cocktail")) {
//                    Log.i("JSON Cocktail", s);

//
//                    Intent intent = new Intent("homeRandomCocktailData");
//                    intent.putExtra("idDrink", idDrink);
//                    intent.putExtra("strDrink",  strDrink);
//                    intent.putExtra("strCategory", strCategory);
//                    intent.putExtra("strAlcoholic", strAlcoholic);
//                    intent.putExtra("strDrinkThumb", strDrinkThumb);
//                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String  downloadFromURL(String url) {
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
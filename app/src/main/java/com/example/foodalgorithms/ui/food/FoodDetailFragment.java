package com.example.foodalgorithms.ui.food;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.foodalgorithms.DownloadImageTask;
import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.home.HomeFragment;
import com.example.foodalgorithms.ui.home.HomeService;

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
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class FoodDetailFragment extends Fragment {

    Food food = new Food(52859, "Key Lime Pie", "American", "Dessert",
            Arrays.asList("Digestive Biscuits", "Butter", "Condensed Milk", "Egg Yolks", "Lime", "Double Cream", "Icing Sugar", "Lime"),
            Arrays.asList("300g","150g", "400g", "3", "4", "300ml ", "1 tbls", "to serve"),
            "Heat the oven to 160C/fan 140C/gas 3. Whizz the biscuits to crumbs in a food processor (or put in a strong plastic bag and bash with a rolling pin). Mix with the melted butter and press into the base and up the sides of a 22cm loose-based tart tin. Bake in the oven for 10 minutes. Remove and cool.\r\nPut the egg yolks in a large bowl and whisk for a minute with electric beaters. Add the condensed milk and whisk for 3 minutes then add the zest and juice and whisk again for 3 minutes. Pour the filling into the cooled base then put back in the oven for 15 minutes. Cool then chill for at least 3 hours or overnight if you like.\r\nWhen you are ready to serve, carefully remove the pie from the tin and put on a serving plate. To decorate, softly whip together the cream and icing sugar. Dollop or pipe the cream onto the top of the pie and finish with extra lime zest.",
            "https://www.themealdb.com/images/media/meals/qpqtuu1511386216.jpg",
            "https://www.youtube.com/watch?v=q4Rz7tUkX9A");

    TextView foodDetailsName;
    TextView foodDetailsCategory;
    TextView foodDetailsCountry;
    TextView foodDetailsIngredientsText;
    TextView foodDetailsDirectionsText;
    ImageView foodDetailsImage;
    WebView foodDetailsVideo;

    int idMeal;

    public static FoodDetailFragment newInstance() {
        return new FoodDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idMeal = bundle.getInt("idMeal");
            Log.i("FOOD DETAILS ID MEAL", idMeal + "");
        }
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        foodDetailsName = view.findViewById(R.id.FoodDetailsName);
        foodDetailsCategory = view.findViewById(R.id.FoodDetailCategory);
        foodDetailsCountry = view.findViewById(R.id.FoodDetailsCountry);
        foodDetailsIngredientsText = view.findViewById(R.id.FoodDetailsIngredientsText);
        foodDetailsDirectionsText = view.findViewById(R.id.FoodDetailsInstructionText);
        foodDetailsImage = view.findViewById(R.id.FoodDetailsImage);
        foodDetailsVideo = view.findViewById(R.id.FoodDetailsVideoView);
        String foodURL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + idMeal;
        new DownloadFoodRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,foodURL);
        return view;
    }


    private class DownloadFoodRecipe extends AsyncTask<String, Void, String> {
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
                    JSONObject meal = jsonObject.getJSONArray("meals").getJSONObject(0);
                    String strMeal = meal.getString("strMeal");
                    String strCategory = meal.getString("strCategory");
                    String strArea = meal.getString("strArea");
                    String strMealThumb = meal.getString("strMealThumb");
                    String strYoutube = meal.getString("strYoutube");
                    StringBuilder ingredientsBuilder = new StringBuilder();
                    for (int i = 1; i <= 20; i++) {
                        String strIngredients = meal.getString("strIngredient" + i);
                        String strMeasure = meal.getString("strMeasure" + i);
                        if (strIngredients.equals("null") || strIngredients.isEmpty()) {
                            break;
                        } else {
                            ingredientsBuilder.append(i).append(". ").append(strMeasure).append(" ").append(strIngredients).append("\n");
                        }
                    }
                    String strInstructions = meal.getString("strInstructions");
                    foodDetailsName.setText(strMeal);
                    foodDetailsCountry.setText(strCategory);
                    foodDetailsCategory.setText(strArea);
                    foodDetailsIngredientsText.setText(ingredientsBuilder.toString());
                    foodDetailsDirectionsText.setText(strInstructions);
                    new DownloadImageTask(foodDetailsImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strMealThumb);

                foodDetailsVideo.setWebViewClient(new WebViewClient());
                foodDetailsVideo.getSettings().setJavaScriptEnabled(true);

                if (!strYoutube.isEmpty()) {
                    String youtubeId = strYoutube.split("=")[1];

                    String customHtml = "<iframe  src=\"https://www.youtube.com/embed/" + youtubeId + "\"  frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
                    foodDetailsVideo.loadData(customHtml, "text/html", "UTF-8");
                } else {
                    String customHtml = "No Youtube Video available for this recipe.";
                    foodDetailsVideo.loadData(customHtml, "text", "UTF-8");
                }
//                } else if (cocktailOrFood.equals("cocktail")) {
//                    Log.i("JSON Cocktail", s);
//                    JSONObject drink = jsonObject.getJSONArray("drinks").getJSONObject(0);
//                    int idDrink = drink.getInt("idDrink");
//                    String strDrink = drink.getString("strDrink");
//                    String strCategory = drink.getString("strCategory");
//                    String strAlcoholic = drink.getString("strAlcoholic");
//                    String strDrinkThumb = drink.getString("strDrinkThumb");
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
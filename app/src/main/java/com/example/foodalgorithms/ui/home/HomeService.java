package com.example.foodalgorithms.ui.home;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.foodalgorithms.ui.search.food.SearchFoodAdapter;

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

public class HomeService extends Service {

    HomeServiceBinder homeServiceBinder;
    Runnable runnable;
    Handler handler = new Handler();
    String jsonData;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return homeServiceBinder;
    }

    public class HomeServiceBinder extends Binder {
        HomeService getHomeService() {
            return HomeService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        runnable = new Runnable() {
            @Override
            public void run() {
                        String foodURL = "https://www.themealdb.com/api/json/v1/1/random.php";
                        String cocktailURL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            new HomeService.DownloadFoodRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,foodURL, "food");
                            new HomeService.DownloadFoodRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,cocktailURL, "cocktail");
                            //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageURL);
                        } else {
                            Toast.makeText(getApplicationContext(), "no network connection", Toast.LENGTH_SHORT).show();
                        }

            }
        };
        handler.post(runnable);
        return START_STICKY;
    }

    private class DownloadFoodRecipe extends AsyncTask<String, Void, String> {
        String cocktailOrFood;

        @Override
        protected String doInBackground(String... urls) {
            jsonData = downloadFromURL(urls[0]);
            cocktailOrFood = urls[1];
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (cocktailOrFood.equals("food")) {
                    Log.i("JSON FOOD", s);
                    JSONObject meal = jsonObject.getJSONArray("meals").getJSONObject(0);
                    int idMeal = meal.getInt("idMeal");
                    String strMeal = meal.getString("strMeal");
                    String strCategory = meal.getString("strCategory");
                    String strArea = meal.getString("strArea");
                    String strMealThumb = meal.getString("strMealThumb");
                    Intent intent = new Intent("homeRandomRecipeData");

                    intent.putExtra("idMeal", idMeal);
                    intent.putExtra("strMeal",  strMeal);
                    intent.putExtra("strCategory", strCategory);
                    intent.putExtra("strArea", strArea);
                    intent.putExtra("strMealThumb", strMealThumb);
                    sendBroadcast(intent);
                } else if (cocktailOrFood.equals("cocktail")) {
                    Log.i("JSON Cocktail", s);
                    JSONObject drink = jsonObject.getJSONArray("drinks").getJSONObject(0);
                    int idDrink = drink.getInt("idDrink");
                    String strDrink = drink.getString("strDrink");
                    String strCategory = drink.getString("strCategory");
                    String strAlcoholic = drink.getString("strAlcoholic");
                    String strDrinkThumb = drink.getString("strDrinkThumb");

                    Intent intent = new Intent("homeRandomCocktailData");
                    intent.putExtra("idDrink", idDrink);
                    intent.putExtra("strDrink",  strDrink);
                    intent.putExtra("strCategory", strCategory);
                    intent.putExtra("strAlcoholic", strAlcoholic);
                    intent.putExtra("strDrinkThumb", strDrinkThumb);
                    sendBroadcast(intent);
                }
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

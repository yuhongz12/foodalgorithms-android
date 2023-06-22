package com.example.foodalgorithms.ui.home;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.foodalgorithms.DownloadImageTask;
import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.cocktail.CocktailDetailFragment;
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

public class HomeFragment extends Fragment {
    CardView randomFoodCard;

    CardView randomCocktailCard;

    HomeService homeServiceRef;
    boolean isBound = false;

    private final ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            HomeService.HomeServiceBinder homeServiceBinder = (HomeService.HomeServiceBinder) service;
            homeServiceRef = homeServiceBinder.getHomeService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        randomFoodCard = view.findViewById(R.id.RandomFoodCard);
        randomCocktailCard = view.findViewById(R.id.RandomCocktailCard);

        BroadcastReceiver foodBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int idMeal = intent.getIntExtra("idMeal", 0);
                String strMeal = intent.getStringExtra("strMeal");
                String strCategory = intent.getStringExtra("strCategory");
                String strArea = intent.getStringExtra("strArea");
                String imageURL = intent.getStringExtra("strMealThumb");
                TextView nameTV = randomFoodCard.findViewById(R.id.RandomFoodName);
                TextView categoryTV = randomFoodCard.findViewById(R.id.RandomFoodCategory);
                TextView countryTV = randomFoodCard.findViewById(R.id.RandomFoodCountry);
                ImageView foodImage = randomFoodCard.findViewById(R.id.RandomFoodImage);
                nameTV.setText(strMeal);
                categoryTV.setText(strCategory);
                countryTV.setText(strArea);
                new DownloadImageTask(foodImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageURL);
                randomFoodCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
                        Bundle arguments = new Bundle();
                        Log.i("Home ID MEAL", idMeal + "");
                        arguments.putInt("idMeal",  idMeal);
                        foodDetailFragment.setArguments(arguments);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, foodDetailFragment).addToBackStack(null).commit();
                    }
                });
            }
        };

        BroadcastReceiver cocktailBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int idDrink = intent.getIntExtra("idDrink", 0);
                String strDrink = intent.getStringExtra("strDrink");
                String strCategory = intent.getStringExtra("strCategory");
                String strAlcoholic = intent.getStringExtra("strAlcoholic");
                String imageURL = intent.getStringExtra("strDrinkThumb");
                Log.i("HOME COCKTAIL IMAGE", imageURL);
                TextView nameTV = randomCocktailCard.findViewById(R.id.RandomCocktailName);
                TextView categoryTV = randomCocktailCard.findViewById(R.id.RandomCocktailCategory);
                TextView countryTV = randomCocktailCard.findViewById(R.id.RandomCocktailCountry);

                ImageView foodImage = randomCocktailCard.findViewById(R.id.RandomCocktailImage);
                nameTV.setText(strDrink);
                categoryTV.setText(strCategory);
                countryTV.setText(strAlcoholic);
                new DownloadImageTask(foodImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageURL);
                randomCocktailCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CocktailDetailFragment cocktailDetailFragment = new CocktailDetailFragment();
                        Bundle arguments = new Bundle();
                        arguments.putInt("idDrink",  idDrink);
                        cocktailDetailFragment.setArguments(arguments);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, cocktailDetailFragment).addToBackStack(null).commit();
                    }
                });
            }
        };
        getContext().registerReceiver(foodBr, new IntentFilter("homeRandomRecipeData"));
        getContext().registerReceiver(cocktailBr, new IntentFilter("homeRandomCocktailData"));
        Intent homeServiceIntent = new Intent(getContext(), HomeService.class);
        getActivity().startService(homeServiceIntent);



        randomCocktailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewComboDetails = new Intent(getContext(), FoodDetailFragment.class);
                AppCompatActivity activity = (AppCompatActivity) getContext();
                FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
                Bundle arguments = new Bundle();
                //   arguments.putString("idMeal", String.valueOf(cocktailItemList.get(holder.getAdapterPosition()).getIdDrink()));
                foodDetailFragment.setArguments(arguments);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, foodDetailFragment).addToBackStack(null).commit();
            }
        });


        return view;
    }

//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap bmp = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                bmp = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return bmp;
//        }
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

}

//    private class DownloadFoodRecipe extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            jsonData = downloadFromURL(urls[0]);
//            return jsonData;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                JSONObject jsonObject = new JSONObject(s);
//                Log.i("JSON HOJME", s);
//                // get city name
////                String city = jsonObject.getString("name");
////                // get the temp, temp_min, temp_max, humidity
////                JSONObject mainObject = jsonObject.getJSONObject("main");
////
////                double temp = mainObject.getDouble("temp");
////                double minTemp = mainObject.getDouble("temp_min");
////                double maxTemp = mainObject.getDouble("temp_max");
////                int humidity = mainObject.getInt("humidity");
////                // get the weather icon
////                JSONArray weatherArray = jsonObject.getJSONArray("weather");
////                JSONObject weatherObject = weatherArray.getJSONObject(0);
////                String weatherIcon = weatherObject.getString("main");
////                System.out.println(weatherIcon);
////                // get the wind speed
////                JSONObject windObject = jsonObject.getJSONObject("wind");
////                double windSpeed = windObject.getDouble("speed");
////
////                // send the information to the broadcast, so that it can generate a notification
////                // create a weather action
//                Intent intent = new Intent("weather");
////                intent.putExtra("city", city);
////                intent.putExtra("temp", temp);
////                intent.putExtra("minTemp", minTemp);
////                intent.putExtra("maxTemp", maxTemp);
////                intent.putExtra("humidity", humidity);
////                intent.putExtra("weatherIcon", weatherIcon);
////                intent.putExtra("windSpeed", windSpeed);
////                intent.putExtra("date", new Date().getTime());
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private String  downloadFromURL(String url) {
//            InputStream is = null;
//            StringBuffer result = new StringBuffer();
//            try {
//                URL myURL = new URL(url);
//                HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();
//                connection.setReadTimeout(3000);
//                connection.setConnectTimeout(3000);
//                connection.setRequestMethod("GET");
//                connection.setDoInput(true);
//                connection.connect();
//                int responseCode = connection.getResponseCode();
//                if (responseCode != HttpsURLConnection.HTTP_OK) {
//                    throw new IOException("HTTP error code: " + responseCode);
//                }
//                is = connection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    result.append(line);
//                }
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            } catch (ProtocolException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return result.toString();
//        }
//    }

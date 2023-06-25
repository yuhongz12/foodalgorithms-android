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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodalgorithms.DownloadImageTask;
import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class FoodDetailFragment extends Fragment {
    TextView foodDetailsName;
    TextView foodDetailsCategory;
    TextView foodDetailsCountry;
    TextView foodDetailsIngredientsText;
    TextView foodDetailsDirectionsText;
    ImageView foodDetailsImage;
    WebView foodDetailsVideo;

    ImageButton likeFoodButton;
    ImageButton shareFoodButton;

    int idMeal;

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
        likeFoodButton = view.findViewById(R.id.LikeFoodRecipeButton);
        shareFoodButton = view.findViewById(R.id.FoodShareButton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users/" + user.getUid() + "like/food/" + idMeal);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean liked = snapshot.child("liked").getValue(Boolean.class);
                    if (liked != null && liked) {
                        likeFoodButton.setImageResource(R.drawable.baseline_star_24);
                    } else {
                        likeFoodButton.setImageResource(R.drawable.baseline_star_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getContext(), "Error. User not logged in", Toast.LENGTH_SHORT).show();
        }

        String foodURL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + idMeal;
        new DownloadFoodRecipe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, foodURL);
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
                String instruction = meal.getString("strInstructions");
                String ingredients = ingredientsBuilder.toString();
                foodDetailsName.setText(strMeal);
                foodDetailsCategory.setText(strCategory);
                foodDetailsCountry.setText(strArea);
                foodDetailsIngredientsText.setText(ingredients);
                foodDetailsDirectionsText.setText(instruction);
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

                likeFoodButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users/" + user.getUid() + "like/food/" + idMeal);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Boolean liked = snapshot.child("liked").getValue(Boolean.class);
                                    if (liked != null && liked) {
                                        userRef.child("liked").setValue(false);
                                        likeFoodButton.setImageResource(R.drawable.baseline_star_border_24);
                                        Toast.makeText(getContext(), "Removed from liked food recipe collection", Toast.LENGTH_SHORT).show();
                                    } else {
                                        userRef.child("liked").setValue(true);
                                        userRef.child("meal").setValue(new ResultFoodItem(idMeal, strMeal, strMealThumb));
                                        likeFoodButton.setImageResource(R.drawable.baseline_star_24);
                                        Toast.makeText(getContext(), "Added to liked food recipe collection", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Error. User not logged in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                shareFoodButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        /*This will be the actual content you wish you share.*/
                        String shareBody = strMeal  + "\n Country: " + strArea +  "\nCategory: " + strCategory + "\n Ingrdients: \n" + ingredients + "\n Instruction: \n" + instruction;
                        /*The type of the content is text, obviously.*/
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this food recipe!");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(intent, "Share Food details using:"));
                    }
                });
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
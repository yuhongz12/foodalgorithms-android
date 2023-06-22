package com.example.foodalgorithms.ui.home;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
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
                        arguments.putInt("idMeal", idMeal);
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
                        arguments.putInt("idDrink", idDrink);
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
}

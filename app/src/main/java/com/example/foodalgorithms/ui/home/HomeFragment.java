package com.example.foodalgorithms.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;

public class HomeFragment extends Fragment {
    CardView randomFoodCard;

    CardView randomCocktailCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        randomFoodCard = view.findViewById(R.id.ComboRecipeRecipeCard);
        randomCocktailCard = view.findViewById(R.id.RandomCocktailRecipeCard);

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

        randomFoodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewComboDetails = new Intent(getContext(), FoodDetailFragment.class);
                AppCompatActivity activity = (AppCompatActivity) getContext();
                FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
                Bundle arguments = new Bundle();
               // arguments.putString("idMeal", String.valueOf(cocktailItemList.get(holder.getAdapterPosition()).getIdDrink()));
                foodDetailFragment.setArguments(arguments);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, foodDetailFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }

}
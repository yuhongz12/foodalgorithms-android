package com.example.foodalgorithms.ui.combo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;

public class ComboDetails extends Fragment {

    String comboId;

    CardView comboFoodCard;
    CardView comboCocktailCard;




    public ComboDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            comboId = bundle.getString("comboId");

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combo_details, container, false);
        TextView tv = view.findViewById(R.id.ComboDetailName);
       // tv.setText(comboId);
        comboFoodCard = view.findViewById(R.id.ComboFoodCard);
        comboCocktailCard= view.findViewById(R.id.ComboCocktailRecipeCard);

        comboFoodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
                Bundle arguments = new Bundle();
                foodDetailFragment.setArguments(arguments);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, foodDetailFragment).addToBackStack(null).commit();
            }
        });

        comboCocktailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
                Bundle arguments = new Bundle();
                foodDetailFragment.setArguments(arguments);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, foodDetailFragment).addToBackStack(null).commit();
            }
        });


        return view;
    }
}
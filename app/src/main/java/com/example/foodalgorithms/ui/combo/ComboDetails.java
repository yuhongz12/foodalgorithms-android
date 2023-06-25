package com.example.foodalgorithms.ui.combo;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodalgorithms.DownloadImageTask;
import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.cocktail.CocktailDetailFragment;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;
import com.example.foodalgorithms.ui.search.cocktail.SearchCocktailAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComboDetails extends Fragment {

    String comboId;

    TextView comboDetailName;
    TextView comboDetailDescription;

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
        TextView comboDetailName = view.findViewById(R.id.ComboDetailName);
        TextView comboDetailDescription = view.findViewById(R.id.ComboDetailDescription);
        comboFoodCard = view.findViewById(R.id.ComboFoodCard);
        comboCocktailCard = view.findViewById(R.id.ComboCocktailRecipeCard);

        DatabaseReference comboRef = FirebaseDatabase.getInstance().getReference().child("combos/" + comboId);
        comboRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("combo Details id", comboId);
                Log.i("combo Details key", snapshot.getKey());
                Combo combo = snapshot.getValue(Combo.class);

                comboDetailName.setText(combo.getComboName());
                comboDetailDescription.setText(combo.getComboDescription());

                ImageView comboFoodImage = comboFoodCard.findViewById(R.id.ComboFoodImage);
                TextView comboFoodName = comboFoodCard.findViewById(R.id.ComboFoodName);
                new DownloadImageTask(comboFoodImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, combo.getComboFood().getStrMealThumb());
                comboFoodName.setText(combo.getComboFood().getStrMeal());

                comboFoodCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
                        Bundle arguments = new Bundle();
                        arguments.putInt("idMeal", combo.getComboFood().getIdMeal());
                        foodDetailFragment.setArguments(arguments);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, foodDetailFragment).addToBackStack(null).commit();
                    }
                });

                ImageView comboCocktailImage = comboCocktailCard.findViewById(R.id.ComboCocktailImage);
                TextView comboCocktailName = comboCocktailCard.findViewById(R.id.ComboCocktailName);
                new DownloadImageTask(comboCocktailImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, combo.getComboCocktail().getStrDrinkThumb());
                comboCocktailName.setText(combo.getComboCocktail().getStrDrink());

                comboCocktailCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CocktailDetailFragment cocktailDetailFragment = new CocktailDetailFragment();
                        Bundle arguments = new Bundle();
                        arguments.putInt("idDrink", combo.getComboCocktail().getIdDrink());
                        cocktailDetailFragment.setArguments(arguments);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewHome, cocktailDetailFragment).addToBackStack(null).commit();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });



        return view;
    }
}
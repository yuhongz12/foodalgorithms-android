package com.example.foodalgorithms.ui.combo;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodalgorithms.DownloadImageTask;
import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.cocktail.CocktailDetailFragment;
import com.example.foodalgorithms.ui.food.FoodDetailFragment;
import com.example.foodalgorithms.ui.search.cocktail.SearchCocktailAdapter;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    ImageButton comboLikeButton;
    ImageButton comboShareButton;


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
        comboDetailName = view.findViewById(R.id.ComboDetailName);
        comboDetailDescription = view.findViewById(R.id.ComboDetailDescription);
        comboFoodCard = view.findViewById(R.id.ComboFoodCard);
        comboCocktailCard = view.findViewById(R.id.ComboCocktailRecipeCard);
        comboLikeButton = view.findViewById(R.id.ComboLIkeButton);
        comboShareButton = view.findViewById(R.id.ComboShareButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users/" + user.getUid() + "like/combo/" + comboId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean liked = snapshot.child("liked").getValue(Boolean.class);
                    if (liked != null && liked) {
                        comboLikeButton.setImageResource(R.drawable.baseline_star_24);
                    } else {
                        comboLikeButton.setImageResource(R.drawable.baseline_star_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getContext(), "Error. User not logged in", Toast.LENGTH_SHORT).show();
        }


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

                comboLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users/" + user.getUid() + "like/combo/" + comboId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Boolean liked = snapshot.child("liked").getValue(Boolean.class);
                                    if (liked != null && liked) {
                                        userRef.child("liked").setValue(false);
                                        comboLikeButton.setImageResource(R.drawable.baseline_star_border_24);
                                        Toast.makeText(getContext(), "Removed from liked combo collection", Toast.LENGTH_SHORT).show();
                                    } else {
                                        userRef.child("liked").setValue(true);
                                        userRef.child("combo").setValue(combo);
                                        comboLikeButton.setImageResource(R.drawable.baseline_star_24);
                                        Toast.makeText(getContext(), "Added to liked combo collection", Toast.LENGTH_SHORT).show();

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

                comboShareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        /*This will be the actual content you wish you share.*/
                        String shareBody = combo.getComboName()  + "\n Description: " + combo.getComboDescription() +  "\nCombo Food: " + combo.getComboFood().getStrMeal() + "\n Combo Drink: " + combo.getComboCocktail().getStrDrink();
                        /*The type of the content is text, obviously.*/
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this combo!");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(intent, "Share Combo details using:"));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });



        return view;
    }
}
package com.example.foodalgorithms.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.cocktail.Cocktail;
import com.example.foodalgorithms.ui.combo.Combo;
import com.example.foodalgorithms.ui.combo.ComboAdapter;
import com.example.foodalgorithms.ui.food.Food;
import com.example.foodalgorithms.ui.search.cocktail.ResultCocktailItem;
import com.example.foodalgorithms.ui.search.cocktail.SearchCocktailAdapter;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;
import com.example.foodalgorithms.ui.search.food.SearchFoodAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView userNameTV;
    TextView userIdTV;
    TextView userComboCount;
    String currentUser;
    RecyclerView profileComboList;

    List<Combo> comboList;
    ComboAdapter comboAdapter;

    TextView userFoodCount;
    TextView userCocktailCount;
    RecyclerView userFoodRecyclerView;
    RecyclerView userCocktailRecyclerView;

    List<ResultFoodItem> foodItemList;
    List<ResultCocktailItem> cocktailItemList;

    SearchFoodAdapter foodAdapter;
    SearchCocktailAdapter cocktailAdapter;

    TextView profileComboLikedCount;
    List<Combo> likeComboList;
    ComboAdapter likeComboAdapter;
    RecyclerView likeComboRecyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameTV = view.findViewById(R.id.ProfileWelcomeName);
        userIdTV = view.findViewById(R.id.profileUserId);
        profileComboList = view.findViewById(R.id.profileComboList);
        userComboCount = view.findViewById(R.id.ProfileComboCount);

        userFoodCount = view.findViewById(R.id.LikeFoodRecipeCount);
        userCocktailCount = view.findViewById(R.id.LikeCocktailRecipeCount);
        userFoodRecyclerView = view.findViewById(R.id.profileFoodList);
        userCocktailRecyclerView = view.findViewById(R.id.profileCocktailList);

        comboList = new ArrayList<>();
        comboAdapter = new ComboAdapter(getContext(), comboList);
        profileComboList.setAdapter(comboAdapter);
        profileComboList.setLayoutManager(new LinearLayoutManager(getContext()));
        profileComboList.setNestedScrollingEnabled(false);

        foodItemList = new ArrayList<>();
        foodAdapter = new SearchFoodAdapter(getContext(), foodItemList);
        userFoodRecyclerView.setAdapter(foodAdapter);
        userFoodRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        userFoodRecyclerView.setNestedScrollingEnabled(false);

        cocktailItemList = new ArrayList<>();
        cocktailAdapter = new SearchCocktailAdapter(getContext(), cocktailItemList);
        userCocktailRecyclerView.setAdapter(cocktailAdapter);
        userCocktailRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        userCocktailRecyclerView.setNestedScrollingEnabled(false);

        profileComboLikedCount = view.findViewById(R.id.profileComboLikedCount);
        likeComboList = new ArrayList<>();
        likeComboAdapter = new ComboAdapter(getContext(), likeComboList);
        likeComboRecyclerView = view.findViewById(R.id.profileLIkeComboList);
        likeComboRecyclerView.setAdapter(likeComboAdapter);
        likeComboRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        likeComboRecyclerView.setNestedScrollingEnabled(false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            currentUser = user.getUid();
            userNameTV.setText("Welcome, " + user.getEmail() + "!");
            userIdTV.setText("User id: " + currentUser);
        } else {
            userNameTV.setText("not logged in");
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        comboList.clear();
        comboAdapter.notifyDataSetChanged();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("combos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Combo combo = ds.getValue(Combo.class);
                    if (currentUser != null && combo != null && currentUser.equals(combo.getUserId())) {
                        comboList.add(combo);
                        comboAdapter.notifyItemInserted(comboList.size());
                        userComboCount.setText("Combo created: " + comboList.size());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference().child("users/" + currentUser + "like/food/");
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot f : snapshot.getChildren()) {
                    Boolean liked = f.child("liked").getValue(Boolean.class);
                    if (liked != null && liked) {
                      foodItemList.add(f.child("meal").getValue(ResultFoodItem.class));
                      foodAdapter.notifyItemInserted(foodItemList.size());
                      userFoodCount.setText("Food recipes liked: " + foodItemList.size());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        DatabaseReference cocktailRef = FirebaseDatabase.getInstance().getReference().child("users/" + currentUser + "like/drink/");
        cocktailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot f : snapshot.getChildren()) {
                    Boolean liked = f.child("liked").getValue(Boolean.class);
                    if (liked != null && liked) {
                        cocktailItemList.add(f.child("drink").getValue(ResultCocktailItem.class));
                        cocktailAdapter.notifyItemInserted(cocktailItemList.size());
                        userCocktailCount.setText("Cocktail recipes liked: " + cocktailItemList.size());

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        likeComboList.clear();
        likeComboAdapter.notifyDataSetChanged();
        DatabaseReference comboRef = FirebaseDatabase.getInstance().getReference().child("users/" + currentUser + "like/combo/");

        comboRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot f : snapshot.getChildren()) {
                    Boolean liked = f.child("liked").getValue(Boolean.class);
                    if (liked != null && liked) {
                        Combo combo = f.child("combo").getValue(Combo.class);
                        likeComboList.add(combo);
                        likeComboAdapter.notifyItemInserted(likeComboList.size());
                        profileComboLikedCount.setText("Combo liked: " + likeComboList.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
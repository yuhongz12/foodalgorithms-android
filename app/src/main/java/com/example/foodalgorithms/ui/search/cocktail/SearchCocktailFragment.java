package com.example.foodalgorithms.ui.search.cocktail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;
import com.example.foodalgorithms.ui.search.food.SearchFoodAdapter;

import java.util.ArrayList;
import java.util.List;


public class SearchCocktailFragment
        extends Fragment {

    List<ResultCocktailItem> cocktailItemList = new ArrayList<>();

    EditText searchCocktailInput;
    Button searchCocktailButton;
    RecyclerView searchCocktailRecyclerView;

    SearchCocktailAdapter cocktailAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_cocktail, container, false);

        cocktailItemList.add(new ResultCocktailItem(12562,"Alice Cocktail", "https://www.thecocktaildb.com/images/media/drink/qyqtpv1468876144.jpg"));
        cocktailItemList.add(new ResultCocktailItem(12562,"Alice Cocktail", "https://www.thecocktaildb.com/images/media/drink/qyqtpv1468876144.jpg"));

        searchCocktailInput = view.findViewById(R.id.SearchCocktailInput);
        searchCocktailButton = view.findViewById(R.id.SearchCocktailButton);
        searchCocktailRecyclerView = view.findViewById(R.id.SearchCocktailRecyclerView);
        cocktailAdapter = new SearchCocktailAdapter(getContext(), cocktailItemList);
        searchCocktailRecyclerView.setAdapter(cocktailAdapter);
        searchCocktailRecyclerView.setLayoutManager( new GridLayoutManager(getContext(),2));

        searchCocktailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = searchCocktailInput.getText().toString();
                if (searchTerm.isEmpty()) {
                    Toast.makeText(getContext(), "Search input cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    // run background service to fetch api search result
                }
            }
        });

        return view;
    }
}
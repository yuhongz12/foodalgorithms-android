package com.example.foodalgorithms.ui.search.food;

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

import java.util.ArrayList;
import java.util.List;

public class SearchFoodFragment extends Fragment {

    List<ResultFoodItem> foodList = new ArrayList<>();

    EditText searchFoodInput;
    Button searchFoodButton;
    RecyclerView searchFoodRecylerView;

    SearchFoodAdapter searchFoodAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        foodList.add(new ResultFoodItem(52959,"Baked salmon with fennel & tomatoes", "https://www.themealdb.com/images/media/meals/1548772327.jpg"));
        foodList.add(new ResultFoodItem(52959,"Baked salmon with fennel & tomatoes", "https://www.themealdb.com/images/media/meals/1548772327.jpg"));

        // Inflate the layout for this fragment
       View  view = inflater.inflate(R.layout.fragment_search_food, container, false);
       searchFoodInput = view.findViewById(R.id.SearchFoodInput);
       searchFoodButton = view.findViewById(R.id.SearchFoodButton);
       searchFoodRecylerView = view.findViewById(R.id.SearchCocktailRecyclerView);
        searchFoodAdapter = new SearchFoodAdapter(getContext(), foodList);
        searchFoodRecylerView.setAdapter(searchFoodAdapter);
        searchFoodRecylerView.setLayoutManager( new GridLayoutManager(getContext(),2));

       searchFoodButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String searchTerm = searchFoodInput.getText().toString();
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
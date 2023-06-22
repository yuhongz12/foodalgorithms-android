package com.example.foodalgorithms.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodalgorithms.R;
import com.example.foodalgorithms.ui.search.cocktail.SearchCocktailFragment;
import com.example.foodalgorithms.ui.search.food.SearchFoodFragment;
import com.google.android.material.tabs.TabLayout;

public class SearchFragment extends Fragment {


    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        tabLayout = view.findViewById(R.id.SearchTabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("FOOD ID" , R.id.SearchFoodTab + "" );
                Log.i("COCKTAIL ID", R.id.SearchCocktailTab + "");
                Log.i("CLICKED TAB SEARCH", tab.getText() + " " + tab.getPosition());
                if (tab.getText() != null && tab.getPosition() == 0) {
                    getChildFragmentManager().beginTransaction().replace(R.id.SearchFragmentContainer, new SearchFoodFragment()).addToBackStack(null).commit();
                } else if (tab.getText() != null && tab.getPosition() == 1){
                    getChildFragmentManager().beginTransaction().replace(R.id.SearchFragmentContainer, new SearchCocktailFragment()).addToBackStack(null).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });


        return view;
    }
}
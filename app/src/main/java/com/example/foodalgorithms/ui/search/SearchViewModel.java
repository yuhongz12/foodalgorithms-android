package com.example.foodalgorithms.ui.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodalgorithms.ui.search.cocktail.ResultCocktailItem;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> foodSearchText;
    private MutableLiveData<ArrayList<ResultFoodItem>> foodSearchResults;

    private MutableLiveData<String> cocktailSearchText;
    private MutableLiveData<ArrayList<ResultCocktailItem>> cocktailSearchResults;

    public SearchViewModel() {
        foodSearchText = new MutableLiveData<>("");
        foodSearchResults = new MutableLiveData<>(new ArrayList<>());
        cocktailSearchText = new MutableLiveData<>("");
        cocktailSearchResults = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<String> getFoodSearchText() {
        return foodSearchText;
    }

    public void setFoodSearchText(MutableLiveData<String> foodSearchText) {
        this.foodSearchText = foodSearchText;
    }

    public MutableLiveData<ArrayList<ResultFoodItem>> getFoodSearchResults() {
        return foodSearchResults;
    }

    public void setFoodSearchResults(MutableLiveData<ArrayList<ResultFoodItem>> foodSearchResults) {
        this.foodSearchResults = foodSearchResults;
    }

    public MutableLiveData<String> getCocktailSearchText() {
        return cocktailSearchText;
    }

    public void setCocktailSearchText(MutableLiveData<String> cocktailSearchText) {
        this.cocktailSearchText = cocktailSearchText;
    }

    public MutableLiveData<ArrayList<ResultCocktailItem>> getCocktailSearchResults() {
        return cocktailSearchResults;
    }

    public void setCocktailSearchResults(MutableLiveData<ArrayList<ResultCocktailItem>> cocktailSearchResults) {
        this.cocktailSearchResults = cocktailSearchResults;
    }
}
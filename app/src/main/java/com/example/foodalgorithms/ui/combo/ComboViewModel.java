package com.example.foodalgorithms.ui.combo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodalgorithms.ui.search.cocktail.ResultCocktailItem;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;

import java.util.ArrayList;

public class ComboViewModel extends ViewModel {

    MutableLiveData<ResultFoodItem> foodItem;
    MutableLiveData<ResultCocktailItem> cocktailItem;
    MutableLiveData<String> comboName;
    MutableLiveData<String> comboDescription;

    public ComboViewModel() {
        foodItem = new MutableLiveData<>(new ResultFoodItem(-1, "", ""));
        cocktailItem = new MutableLiveData<>(new ResultCocktailItem(-1, "", ""));
        comboName = new MutableLiveData<>("");
        comboDescription = new MutableLiveData<>("");
    }

    public MutableLiveData<ResultFoodItem> getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(MutableLiveData<ResultFoodItem> foodItem) {
        this.foodItem = foodItem;
    }

    public MutableLiveData<ResultCocktailItem> getCocktailItem() {
        return cocktailItem;
    }

    public void setCocktailItem(MutableLiveData<ResultCocktailItem> cocktailItem) {
        this.cocktailItem = cocktailItem;
    }

    public MutableLiveData<String> getComboName() {
        return comboName;
    }

    public void setComboName(MutableLiveData<String> comboName) {
        this.comboName = comboName;
    }

    public MutableLiveData<String> getComboDescription() {
        return comboDescription;
    }

    public void setComboDescription(MutableLiveData<String> comboDescription) {
        this.comboDescription = comboDescription;
    }
}
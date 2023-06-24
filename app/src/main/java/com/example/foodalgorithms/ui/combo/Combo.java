package com.example.foodalgorithms.ui.combo;

import com.example.foodalgorithms.ui.search.cocktail.ResultCocktailItem;
import com.example.foodalgorithms.ui.search.food.ResultFoodItem;

public class Combo {
    String comboId;
    String comboName;
    String comboDescription;
    ResultFoodItem comboFood;
    ResultCocktailItem comboCocktail;

    public Combo() {

    }

    public Combo(String comboId, String comboName, String comboDescription, ResultFoodItem comboFood, ResultCocktailItem comboCocktail) {
        this.comboId = comboId;
        this.comboName = comboName;
        this.comboDescription = comboDescription;
        this.comboFood = comboFood;
        this.comboCocktail = comboCocktail;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public ResultFoodItem getComboFood() {
        return comboFood;
    }

    public void setComboFood(ResultFoodItem comboFood) {
        this.comboFood = comboFood;
    }

    public ResultCocktailItem getComboCocktail() {
        return comboCocktail;
    }

    public void setComboCocktail(ResultCocktailItem comboCocktail) {
        this.comboCocktail = comboCocktail;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }

    public String getComboDescription() {
        return comboDescription;
    }

    public void setComboDescription(String comboDescription) {
        this.comboDescription = comboDescription;
    }
}

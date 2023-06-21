package com.example.foodalgorithms.ui.combo;

import com.example.foodalgorithms.ui.cocktail.Cocktail;
import com.example.foodalgorithms.ui.food.Food;

public class Combo {

    String comboId;
    String comboName;
    String comboFood;
    String comboCocktail;

    public Combo(String comboName, String comboFood, String comboCocktail) {
        this.comboName = comboName;
        this.comboFood = comboFood;
        this.comboCocktail = comboCocktail;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public String getComboFood() {
        return comboFood;
    }

    public void setComboFood(String comboFood) {
        this.comboFood = comboFood;
    }

    public String getComboCocktail() {
        return comboCocktail;
    }

    public void setComboCocktail(String comboCocktail) {
        this.comboCocktail = comboCocktail;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }
}

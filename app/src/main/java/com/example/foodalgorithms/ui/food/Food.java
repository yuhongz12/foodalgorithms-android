package com.example.foodalgorithms.ui.food;

import java.util.List;

public class Food {

    int idMeal;

    String name;

    String country;

    String category;
    String imageURL;


    List<String> strIngredients;
    List<String> strMeasures;

    String direction;
    String videoURL;

    public Food(int idMeal, String name, String country, String category, List<String> strIngredients, List<String> strMeasures, String direction, String imageURL) {
        this.idMeal = idMeal;
        this.name = name;
        this.imageURL = imageURL;
        this.country = country;
        this.category = category;
        this.strIngredients = strIngredients;
        this.strMeasures = strMeasures;
        this.direction = direction;
    }

    public Food(int idMeal, String name, String country, String category, List<String> strIngredients, List<String> strMeasures, String direction, String imageURL, String videoURL) {
        this.idMeal = idMeal;
        this.name = name;
        this.imageURL = imageURL;
        this.country = country;
        this.category = category;
        this.strIngredients = strIngredients;
        this.strMeasures = strMeasures;
        this.direction = direction;
        this.videoURL = videoURL;
    }

    public int getIdMeal() {
        return idMeal;
    }


    public void setIdMeal(int idMeal) {
        this.idMeal = idMeal;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getStrIngredients() {
        return strIngredients;
    }

    public void setStrIngredients(List<String> strIngredients) {
        this.strIngredients = strIngredients;
    }

    public List<String> getStrMeasures() {
        return strMeasures;
    }

    public void setStrMeasures(List<String> strMeasures) {
        this.strMeasures = strMeasures;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }
}

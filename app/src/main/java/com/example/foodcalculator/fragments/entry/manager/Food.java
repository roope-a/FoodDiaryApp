package com.example.foodcalculator.fragments.entry.manager;

import java.time.LocalDate;

public class Food {

    private String foodName;
    private String mealType;
    private double calories;
    private double fat;
    private double sodium;
    private double carbs;
    private double sugar;
    private double fiber;
    private double protein;
    private LocalDate localDate;

    //Constructor used for most actions, like writing the file
    public Food (String foodName, String mealType, double calories, double fat, double sodium, double carbs, double sugar, double fiber, double protein) {
        this.foodName = foodName;
        this.mealType = mealType;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fiber = fiber;
        this.protein = protein;
    }

    //Special constructor for returning dates with the food items
    public Food (String foodName, String mealType, double calories, double fat, double sodium, double carbs, double sugar, double fiber, double protein, LocalDate localDate) {
        this.foodName = foodName;
        this.mealType = mealType;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fiber = fiber;
        this.protein = protein;
        this.localDate = localDate;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getMealType() {
        return mealType;
    }

    public double getCalories() {
        return calories;
    }

    public double getFat() {
        return fat;
    }

    public double getSodium() {
        return sodium;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getSugar() {
        return sugar;
    }

    public double getFiber() {
        return fiber;
    }

    public double getProtein() {
        return protein;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
}

package com.example.foodcalculator.fragments.entry.manager;

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

    ///

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

    // Might remove these

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setMealType(String mealType) {
       this.mealType = mealType;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }
}

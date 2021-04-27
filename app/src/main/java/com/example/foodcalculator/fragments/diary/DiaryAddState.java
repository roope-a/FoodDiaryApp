package com.example.foodcalculator.fragments.diary;

import androidx.annotation.Nullable;

public class DiaryAddState {

    @Nullable
    private Integer foodNameError;
    @Nullable
    private Integer mealTypeError;
    @Nullable
    private Integer caloriesError;
    @Nullable
    private Integer fatsError;
    @Nullable
    private Integer sodiumError;
    @Nullable
    private Integer carbsError;
    @Nullable
    private Integer sugarsError;
    @Nullable
    private Integer fibersError;
    @Nullable
    private Integer proteinError;
    @Nullable
    private Integer gramsError;

    private boolean isDataValid;

    DiaryAddState(@Nullable Integer foodNameError, @Nullable Integer mealTypeError, @Nullable Integer caloriesError,
                  @Nullable Integer fatsError, @Nullable Integer sodiumError, @Nullable Integer carbsError,
                  @Nullable Integer sugarsError, @Nullable Integer fibersError, @Nullable Integer proteinError,
                  @Nullable Integer gramsError) {
        this.foodNameError = foodNameError;
        this.mealTypeError = mealTypeError;
        this.caloriesError = caloriesError;
        this.fatsError = fatsError;
        this.sodiumError = sodiumError;
        this.carbsError = carbsError;
        this.sugarsError = sugarsError;
        this.fibersError = fibersError;
        this.proteinError = proteinError;
        this.gramsError = gramsError;
        this.isDataValid = false;
    }

    DiaryAddState(boolean isDataValid) {
        this.foodNameError = null;
        this.caloriesError = null;
        this.fatsError = null;
        this.sodiumError = null;
        this.carbsError = null;
        this.sugarsError = null;
        this.fibersError = null;
        this.proteinError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getFoodNameError() {
        return foodNameError;
    }

    @Nullable
    Integer getCaloriesError() {
        return caloriesError;
    }

    @Nullable
    Integer getFatsError() {
        return fatsError;
    }

    @Nullable
    Integer getSodiumError() {
        return sodiumError;
    }

    @Nullable
    Integer getCarbsError() {
        return carbsError;
    }

    @Nullable
    Integer getSugarsError() {
        return sugarsError;
    }

    @Nullable
    Integer getFibersError() {
        return fibersError;
    }

    @Nullable
    Integer getProteinError() {
        return proteinError;
    }

    @Nullable
    Integer getGramsError() {
        return gramsError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}

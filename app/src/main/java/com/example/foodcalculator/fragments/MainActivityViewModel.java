package com.example.foodcalculator.fragments;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcalculator.R;


public class MainActivityViewModel extends ViewModel {

    //private MutableLiveData<String> mName = new MutableLiveData<>();
    private MutableLiveData<DiaryAddState> diaryAddState = new MutableLiveData<>();

    LiveData<DiaryAddState> getDiaryAddState() {return diaryAddState;}


//    LiveData<DiaryAddState> getLoginFormState() {
//        return DiaryAddState;
//    }

    public void diaryAddDataChanged(String foodName, String mealtype,double calories, double fats, double sodium, double carbs, double sugars, double fibers, double protein, double grams) {

        if (!isFoodNameValid(foodName)) {
            diaryAddState.setValue(new DiaryAddState(R.string.invalid_food, null,null, null, null, null, null, null, null, null));
        } else if (!isMealtypeValid(mealtype)) {
            diaryAddState.setValue(new DiaryAddState(null, R.string.invalid_mealtype, null ,null, null, null, null, null, null, null));
        } else if (!isNutritionValid(calories)) {
            diaryAddState.setValue(new DiaryAddState(null, null, R.string.invalid_nutrition, null, null, null, null, null, null, null));
        } else if (!isNutritionValid(fats)) {
            diaryAddState.setValue(new DiaryAddState(null, null, null, R.string.invalid_nutrition, null, null, null, null, null, null));
        } else if (!isNutritionValid(sodium)) {
            diaryAddState.setValue(new DiaryAddState(null, null, null, null, R.string.invalid_nutrition, null, null, null, null, null));
        } else if (!isNutritionValid(carbs)) {
            diaryAddState.setValue(new DiaryAddState(null, null, null, null, null, R.string.invalid_nutrition, null, null, null, null));
        } else if (!isNutritionValid(sugars)) {
            diaryAddState.setValue(new DiaryAddState(null, null, null, null, null, null, R.string.invalid_nutrition, null, null, null));
        } else if (!isNutritionValid(fibers)) {
            diaryAddState.setValue(new DiaryAddState(null, null, null, null, null, null, null, R.string.invalid_nutrition, null, null));
        } else if (!isNutritionValid(protein)) {
            diaryAddState.setValue(new DiaryAddState(null, null, null, null, null, null, null, null, R.string.invalid_nutrition, null));
        } else if (!isMealsizeValid(grams)) {
            diaryAddState.setValue(new DiaryAddState(null, null, null, null, null, null, null, null, null, R.string.invalid_nutrition));
        } else {
            diaryAddState.setValue(new DiaryAddState(true));
        }
    }

    public boolean isFoodNameValid(String foodName) {
        if (TextUtils.isEmpty(foodName)) {
            return false;
        }
        else if (foodName.length() >= 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNutritionValid(double nutrition) {
        if (nutrition < 0) {
            return false;
        }
        else if (nutrition >= 0) {
            return true;
        } else {
            return false;
        }
        //return true;
    }

    public boolean isMealsizeValid(double mealsize) {
        if (mealsize < 0) {
            return false;
        }
        else if (mealsize > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMealtypeValid(String mealType) {

        if (TextUtils.isEmpty(mealType)) {
            return false;
        } else {
            return true;
        }
    }
}

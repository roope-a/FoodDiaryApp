package com.example.foodcalculator.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodcalculator.R;
import com.example.foodcalculator.fragments.entry.manager.EntryManager;
import com.example.foodcalculator.fragments.entry.manager.Food;
import com.example.foodcalculator.fragments.entry.manager.FoodListManager;
import com.example.foodcalculator.httpHandler.AsyncResponse;
import com.example.foodcalculator.httpHandler.HttpHandler;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;



import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DiaryFragmentAdd extends BaseFragment {

    private MainActivityViewModel mainActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        FoodListManager foodListManager = new FoodListManager(view.getContext());
        EntryManager entryManager = new EntryManager(view.getContext());
        AutoCompleteTextView foodMenu = view.findViewById(R.id.foodFinder);
        AutoCompleteTextView mealtypeMenu = view.findViewById(R.id.mealtype);
        AutoCompleteTextView caloriesText = view.findViewById(R.id.calories);
        AutoCompleteTextView fatsText = view.findViewById(R.id.fat);
        AutoCompleteTextView sodiumText = view.findViewById(R.id.sodium);
        AutoCompleteTextView carbsText = view.findViewById(R.id.carbs);
        AutoCompleteTextView sugarsText = view.findViewById(R.id.sugars);
        AutoCompleteTextView fibersText = view.findViewById(R.id.fibers);
        AutoCompleteTextView proteinText = view.findViewById(R.id.protein);
        AutoCompleteTextView mealsizeText = view.findViewById(R.id.mealSize);
        LoadingScreen loadingScreen = new LoadingScreen(getActivity());
        TextView onlineLookUp = view.findViewById(R.id.onlineLookUp);

        ExtendedFloatingActionButton fab = view.findViewById(R.id.addFoodButton);
        fab.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (!isEmptyAutoTextView(foodMenu) && !isEmptyAutoTextView(mealtypeMenu)) {

                    double cal = 0, fat = 0, sod = 0, car = 0, sug = 0, fib = 0, pro = 0, size = 0;

                    //Get all the text field input
                    if (!isEmptyAutoTextView(caloriesText)) {
                        cal = Double.parseDouble(caloriesText.getText().toString());
                    } if (!isEmptyAutoTextView(fatsText)) {
                        fat = Double.parseDouble(fatsText.getText().toString());
                    } if (!isEmptyAutoTextView(sodiumText)) {
                        sod = Double.parseDouble(sodiumText.getText().toString());
                    } if (!isEmptyAutoTextView(carbsText)) {
                        car = Double.parseDouble(carbsText.getText().toString());
                    } if (!isEmptyAutoTextView(sugarsText)) {
                        sug = Double.parseDouble(sugarsText.getText().toString());
                    } if (!isEmptyAutoTextView(fibersText)) {
                        fib = Double.parseDouble(fibersText.getText().toString());
                    } if (!isEmptyAutoTextView(proteinText)) {
                        pro = Double.parseDouble(proteinText.getText().toString());
                    } if (!isEmptyAutoTextView(mealsizeText)) {
                        size = Double.parseDouble(mealsizeText.getText().toString());
                    }
                    entryManager.writeEntry(new Food(foodMenu.getText().toString(),
                            mealtypeMenu.getText().toString(), cal, fat, sod,car,sug,fib,pro),size);
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        onlineLookUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start the loading animation
                loadingScreen.startLoading();

                // Async http request
                HttpHandler httpHandler = new HttpHandler(new AsyncResponse() {
                    @Override
                    public void processFinish(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            loadingScreen.dismissLoading();
                            caloriesText.setText(jsonObject.getJSONArray("items").getJSONObject(0).getString("calories"));
                            fatsText.setText(jsonObject.getJSONArray("items").getJSONObject(0).getString("fat_total_g"));
                            double tempSodium = Double.parseDouble(jsonObject.getJSONArray("items").getJSONObject(0).getString("sodium_mg"));
                            sodiumText.setText(String.valueOf(tempSodium/1000));
                            carbsText.setText(jsonObject.getJSONArray("items").getJSONObject(0).getString("carbohydrates_total_g"));
                            sugarsText.setText(jsonObject.getJSONArray("items").getJSONObject(0).getString("sugar_g"));
                            fibersText.setText(jsonObject.getJSONArray("items").getJSONObject(0).getString("fiber_g"));
                            proteinText.setText(jsonObject.getJSONArray("items").getJSONObject(0).getString("protein_g"));
                            mealsizeText.setText(jsonObject.getJSONArray("items").getJSONObject(0).getString("serving_size_g"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            new AlertDialog.Builder(getActivity()).setMessage("Sorry. We couldn't find what you were looking for :(").create().show();
                        }
                    }
                });

                //Check if user entered meal size, can be used for API query
                if (!isEmptyAutoTextView(mealsizeText)) {
                    httpHandler.execute(mealsizeText.getText().toString()+"grams "+foodMenu.getText().toString());
                } else {
                    httpHandler.execute(foodMenu.getText().toString());
                }
            }
        });

        List<String[]> foodsList;
        ArrayList<String> foodArray = new ArrayList<>();
        Map<String, Food> nutritionalMap = new HashMap<>();

        //Load autocomplete texts and matching food nutritional values into map
        try {
            foodsList = foodListManager.read();
            for (int i = 0; i < foodsList.size(); i++) {
                String[] temp = Arrays.toString(foodsList.get(i)).replace("[", "").replace("]", "").split(";");
                nutritionalMap.put(temp[0], new Food(temp[0], temp[1], Double.parseDouble(temp[2]), Double.parseDouble(temp[3]),
                        Double.parseDouble(temp[4]), Double.parseDouble(temp[5]), Double.parseDouble(temp[6]),
                        Double.parseDouble(temp[7]), Double.parseDouble(temp[8])));
                foodArray.add(temp[0]);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error: "+e);
        }

        List<String> mealtypes = Arrays.asList(getResources().getStringArray(R.array.mealtypes));

//        DEMO
//        CREATES A DEMO LIST FOR AUTOCOMPLETE TEXT FIELD

//        String[] tempfoods = getResources().getStringArray(R.array.temp);

//        for (int i = 0; i < 10; i++) {
//            foodListManager.write(tempfoods[i], mealtypes[ThreadLocalRandom.current().nextInt(0,6+1)],
//                    ThreadLocalRandom.current().nextInt(200,400+1),  ThreadLocalRandom.current().nextInt(200,400+1),
//                    ThreadLocalRandom.current().nextInt(200,400+1), ThreadLocalRandom.current().nextInt(200,400+1),
//                    ThreadLocalRandom.current().nextInt(200,400+1), ThreadLocalRandom.current().nextInt(200,400+1),
//                    ThreadLocalRandom.current().nextInt(200,400+1));
//        }

//        END DEMO

        ArrayAdapter<String> adapter_meal = new ArrayAdapter<>(view.getContext(), R.layout.dropdown_menu, mealtypes);
        mealtypeMenu.setText(R.string.mealtype_default);
        mealtypeMenu.setAdapter(adapter_meal);
        ArrayAdapter<String> adapter_food = new ArrayAdapter<>(view.getContext(), R.layout.dropdown_menu, foodArray);
        foodMenu.setAdapter(adapter_food);

        mainActivityViewModel.getDiaryAddState().observe(getViewLifecycleOwner(), new Observer<DiaryAddState>() {
            @Override
            public void onChanged(DiaryAddState diaryAddState) {
                if (diaryAddState == null) {
                    return;
                }
                fab.setEnabled(diaryAddState.isDataValid());
                // Couldn't get error messages working properly
//
//                if (diaryAddState.getFoodNameError() != null) {
//                    textInputLayout.setError(getString(diaryAddState.getFoodNameError()));
//                }
//                if (diaryAddState.getCaloriesError() != null) {
//                    caloriesText.setError(getString(diaryAddState.getCaloriesError()));
//                }
            }
        });

        foodMenu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int id = v.getId();
                if (id == R.id.foodFinder && !hasFocus) {

                    String foodName = foodMenu.getText().toString();

                    if (nutritionalMap.containsKey(foodName)) {
                        Food foodItem = nutritionalMap.get(foodName);
                        caloriesText.setText(String.valueOf(foodItem.getCalories()));
                        fatsText.setText(String.valueOf(foodItem.getFat()));
                        sodiumText.setText(String.valueOf(foodItem.getSodium()));
                        carbsText.setText(String.valueOf(foodItem.getCarbs()));
                        sugarsText.setText(String.valueOf(foodItem.getSugar()));
                        fibersText.setText(String.valueOf(foodItem.getFiber()));
                        proteinText.setText(String.valueOf(foodItem.getProtein()));
                    }
                }
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

                double cal = 0, fat = 0, sod = 0, car = 0, sug = 0, fib = 0, pro = 0, size = 0;

                //Get all the text field input
                if (!isEmptyAutoTextView(caloriesText)) {
                    cal = Double.parseDouble(caloriesText.getText().toString());
                } if (!isEmptyAutoTextView(fatsText)) {
                    fat = Double.parseDouble(fatsText.getText().toString());
                } if (!isEmptyAutoTextView(sodiumText)) {
                    sod = Double.parseDouble(sodiumText.getText().toString());
                } if (!isEmptyAutoTextView(carbsText)) {
                    car = Double.parseDouble(carbsText.getText().toString());
                } if (!isEmptyAutoTextView(sugarsText)) {
                    sug = Double.parseDouble(sugarsText.getText().toString());
                } if (!isEmptyAutoTextView(fibersText)) {
                    fib = Double.parseDouble(fibersText.getText().toString());
                } if (!isEmptyAutoTextView(proteinText)) {
                    pro = Double.parseDouble(proteinText.getText().toString());
                } if (!isEmptyAutoTextView(mealsizeText)) {
                    size = Double.parseDouble(mealsizeText.getText().toString());
                }

                //Update when data has changed
                mainActivityViewModel.diaryAddDataChanged(foodMenu.getText().toString(),
                        mealtypeMenu.getText().toString(), cal, fat, sod, car, sug, fib, pro, size);
            }
        };
        //Watching all the text fields for input
        foodMenu.addTextChangedListener(textWatcher);
        caloriesText.addTextChangedListener(textWatcher);
        fatsText.addTextChangedListener(textWatcher);
        sodiumText.addTextChangedListener(textWatcher);
        carbsText.addTextChangedListener(textWatcher);
        sugarsText.addTextChangedListener(textWatcher);
        fibersText.addTextChangedListener(textWatcher);
        proteinText.addTextChangedListener(textWatcher);
        mealsizeText.addTextChangedListener(textWatcher);
    }

    public boolean isEmptyAutoTextView(AutoCompleteTextView toCheck) {
        return TextUtils.isEmpty(toCheck.getText().toString());
    }

}

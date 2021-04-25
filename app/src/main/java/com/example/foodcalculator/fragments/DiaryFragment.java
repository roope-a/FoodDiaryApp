package com.example.foodcalculator.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodcalculator.R;
import com.example.foodcalculator.fragments.entry.manager.EntryManager;
import com.example.foodcalculator.fragments.entry.manager.Food;
import com.facebook.stetho.common.ArrayListAccumulator;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiaryFragment extends BaseFragment implements View.OnClickListener {

    private MainActivityViewModel mainActivityViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        FloatingActionButton fab = view.findViewById(R.id.addButton);
        fab.setOnClickListener(this);

        EntryManager entryManager = new EntryManager(view.getContext());

        //Custom date formatter, because the original ones are nitpicky about everything
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .parseLenient().appendPattern("dd.MM.yyyy").toFormatter(Locale.ENGLISH);

        //Set current date to get pie chart info
        LocalDate date = LocalDate.now();
        LocalDate start = LocalDate.parse(formatter.format(date), formatter);
        LocalDate end = LocalDate.parse(formatter.format(date), formatter);

        List<Food> dailyList = entryManager.readEntries(start, end);

        //Get the different calorie counts

        float breakfastCalories = 0, lunchCalories = 0, dinnerCalories = 0,
                snackCalories = 0, junkfoodCalories = 0, treatCalories = 0;

        for (int i = 0; i < dailyList.size(); i++) {

            if (dailyList.get(i).getMealType().equals("Breakfast")) {
                breakfastCalories += dailyList.get(i).getCalories();
            } else if (dailyList.get(i).getMealType().equals("Lunch")) {
                lunchCalories += dailyList.get(i).getCalories();
            } else if (dailyList.get(i).getMealType().equals("Dinner")) {
                dinnerCalories += dailyList.get(i).getCalories();
            } else if (dailyList.get(i).getMealType().equals("Snack")) {
                snackCalories += dailyList.get(i).getCalories();
            } else if (dailyList.get(i).getMealType().equals("Junkfood")) {
                junkfoodCalories += dailyList.get(i).getCalories();
            } else if (dailyList.get(i).getMealType().equals("Treats")) {
                treatCalories += dailyList.get(i).getCalories();
            }
        }
        String[] mealtypeName = {"Breakfast", "Lunch", "Dinner", "Snack", "Junkfood", "Treats"};
        float[] calories = {breakfastCalories, lunchCalories, dinnerCalories, snackCalories, junkfoodCalories, treatCalories};

        //Create the piechart
        PieChart pieChart = view.findViewById(R.id.pieChart);
        pieChart.setRotationEnabled(true); // enable rotation of the chart by touch
        pieChart.setHoleRadius(50);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

        pieChart.animateY(1400, Easing.EaseInOutQuad);
        addDataSet(pieChart, calories , mealtypeName);
    }

    private void addDataSet(PieChart pieChart, float[] calories, String[] mealtype) {
        ArrayList<PieEntry> yEntry = new ArrayList<>();

        for (int i = 0; i < calories.length; i++) {
            if (calories[i] > 0) {
                yEntry.add(new PieEntry(calories[i], mealtype[i]));
            }
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry, "Calories per mealtype");
        pieDataSet.setSliceSpace(10);
        pieDataSet.setValueTextSize(20);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.addButton) {
            //Opening the add food fragment, also passing some default values
            mainActivityViewModel.diaryAddDataChanged("", "", 0, 0, 0, 0, 0, 0, 0, 0);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragmentAdd()).addToBackStack(null).commit();
        }
    }
}

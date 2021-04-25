package com.example.foodcalculator.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.foodcalculator.R;
import com.example.foodcalculator.fragments.entry.manager.EntryManager;
import com.example.foodcalculator.fragments.entry.manager.Food;

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class DiaryListFragment extends BaseFragment {

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listview);
        AutoCompleteTextView startDate = view.findViewById(R.id.startDate);
        AutoCompleteTextView endDate = view.findViewById(R.id.endDate);
        Button searchButton = view.findViewById(R.id.searchButton);

        //Custom date formatter, because the original ones are nitpicky about everything
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
                .parseLenient().appendPattern("dd.MM.yyyy").toFormatter(Locale.ENGLISH);

        //Set current date to fill the list
        LocalDate date = LocalDate.now();
        startDate.setText(formatter.format(date));
        endDate.setText(formatter.format(date));

        LocalDate start = LocalDate.parse(startDate.getText(), formatter);
        LocalDate end = LocalDate.parse(endDate.getText(), formatter);

        EntryManager entryManager = new EntryManager(view.getContext());

        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int mYear, mMonth, mDay;

                if (hasFocus) {

                    Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    startDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    datePickerDialog.show();
                }
            }
        });

        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                int mYear, mMonth, mDay;

                if (hasFocus) {

                    Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Get epoch time to limit datedialogpicker mindatevalue
                    LocalDate start = LocalDate.parse(startDate.getText(), formatter);
                    Instant instant = start.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
                    long timeInMillis = instant.toEpochMilli();

                    DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    endDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMinDate(timeInMillis);
                    datePickerDialog.show();

                }

            }
        });

        List<Food> foodsList = entryManager.readEntries(start, end);
        List<String> stringList = foodsList.stream().map(Food::getFoodName).collect(Collectors.toList());

        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_expandable_list_item_1, stringList);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update listview on search button
                foodsList.clear();
                stringList.clear();
                LocalDate start = LocalDate.parse(startDate.getText(),formatter);
                LocalDate end = LocalDate.parse(endDate.getText(),formatter);
                foodsList.addAll(entryManager.readEntries(start, end));
                foodsList.stream().map(Food::getFoodName).collect(Collectors.toList());
                stringList.addAll(foodsList.stream().map(Food::getFoodName).collect(Collectors.toList()));
                listViewAdapter.notifyDataSetChanged();
            }
        });


        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Set Diary listview info per 'card'
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setMessage(foodsList.get(position).getFoodName() + "\n" + foodsList.get(position).getLocalDate() + "\n" +
                                foodsList.get(position).getMealType() + "\n" + "Calories: \t\t" + foodsList.get(position).getCalories() + " grams\n" +
                                "Protein: \t\t" + foodsList.get(position).getProtein() + " grams\n" + "Fat: \t\t" + foodsList.get(position).getFat() + " grams\n" +
                                "Sugar: \t\t" + foodsList.get(position).getSugar() + " grams\n" + "Fiber: \t\t" + foodsList.get(position).getFiber() + " grams\n" +
                                "Carbohydrates: \t\t" + foodsList.get(position).getCarbs() + " grams\n" + "Sodium: \t\t" + foodsList.get(position).getSodium() + "mg\n");

                builder.create().show();

                parent.getItemAtPosition(position);
            }
        });
    }

}


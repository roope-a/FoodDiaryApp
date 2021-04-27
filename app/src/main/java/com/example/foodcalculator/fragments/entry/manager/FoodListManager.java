package com.example.foodcalculator.fragments.entry.manager;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FoodListManager {

    private Context context;

    public FoodListManager(Context context) {
        this.context = context;
    }

    final private String CSV_NAME = "foods.csv";

    //Create the file if it doesn't exist
    public boolean doesExist() {
        final String filepath = context.getFilesDir().getPath()+"/"+CSV_NAME;
        File file = new File(filepath);
        boolean val = false;
            if (!file.exists()) {
                try {
                    val = true;
                    FileWriter fileWriter = new FileWriter(file, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    PrintWriter printWriter = new PrintWriter(bufferedWriter);

                    printWriter.println("foodname;calories;fat;sodium;carbs;sugar;fiber;protein");
                    printWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                val = false;
            }
            return val;
    }

    public List<String[]> read() {
        List<String[]> resultList = new ArrayList<>();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(context.getFilesDir().getPath()+"/"+CSV_NAME);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error opening input stream: "+e);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            bufferedReader.readLine();
            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading foods list: "+e);
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing input stream: "+e);
            }
        }
        return resultList;
    }


    /*
    * Unimplemented feature, wanted for the user to be able to input foods to autocomplete aswell
    *
    * */

    public void write(Food food) {

        PrintWriter printWriter;
        final String filepath = context.getFilesDir().getPath()+"/"+CSV_NAME;
        File file = new File(filepath);

        try {
            char sep = ';';
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            //        printWriter.println(foodName+ sep+
//                    calories+ sep+
//                    fat+ sep+
//                    sodium+ sep+
//                    carbs+ sep+
//                    sugar+ sep+
//                    fiber+ sep
//                    +protein);

            printWriter.println(food.getFoodName() +sep+
                    food.getCalories() +sep+
                    food.getFat() +sep+
                    food.getSodium() +sep+
                    food.getCarbs() +sep+
                    food.getSugar() +sep+
                    food.getFiber() +sep+
                    food.getProtein());

            printWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

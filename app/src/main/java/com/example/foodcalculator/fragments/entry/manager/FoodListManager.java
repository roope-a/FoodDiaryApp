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

public class FoodListManager {

    private Context context;

    public FoodListManager(Context context) {
        this.context = context;
    }

    final String CSV_NAME = "foods.csv";

    public void doesExist() {
        final String filepath = context.getFilesDir().getPath()+"/"+CSV_NAME;
        File file = new File(filepath);
        try {
            if (!file.exists()) {

                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter);

                printWriter.println("foodname,mealType,calories,fat,sodium,carbs,sugar,fiber,protein");

                printWriter.close();
            }
        } catch (Exception e) {
            //TODO error handling
            e.printStackTrace();
        }

    }

    public List<String[]> read() {
        List<String[]> resultList = new ArrayList<String[]>();
        InputStream inputStream = null;
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

//    public String getFoodName() {
//
//    }

    public void write(String foodName, String mealType, double calories, double fat, double sodium, double carbs, double sugar, double fiber, double protein) {

        PrintWriter printWriter;
        final String filepath = context.getFilesDir().getPath()+"/"+CSV_NAME;
        File file = new File(filepath);

        try {
            Character sep = ';';
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
            // "foodname,ID,mealType,calories,fat,sodium,carbs,sugar,fiber,protein\n"

            printWriter.println(foodName+sep+mealType+sep+calories+sep+fat+sep+sodium+sep+carbs+sep+sugar+sep+fiber+sep+protein);

            printWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error closing input stream: "+e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

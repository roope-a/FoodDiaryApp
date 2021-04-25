package com.example.foodcalculator.fragments.entry.manager;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EntryManager {

    private Context context;

    // TODO ask for user id
    public EntryManager(Context context) {
        this.context = context;
    }

    // TODO add user id usage
    final String CSV_NAME = "userinfo.csv";

    final private Character sep = ';';

    public void doesExist() {
        final String filepath = context.getFilesDir().getPath()+"/"+ CSV_NAME;
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                Character sep = ';';
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter);

                printWriter.println("date" +sep+ "foodName" +sep+ "mealType" +sep+ "calories" +sep+ "fat"
                        +sep+ "sodium" +sep+ "carbohydrates" +sep+ "sugar" +sep+ "fiber" +sep+ "protein"  +sep+ "mealSize");

                printWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeEntry(Food foodItem, double mealSize) {
        final String filepath = context.getFilesDir().getPath()+"/"+ CSV_NAME;
        File file = new File(filepath);
        PrintWriter printWriter;

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(currentDate);

        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);

            printWriter.println(date +sep+ foodItem.getFoodName() +sep+ foodItem.getMealType() +sep+ foodItem.getCalories() +sep+
                    foodItem.getFat() +sep+ foodItem.getSodium() +sep+ foodItem.getCarbs() +sep+ foodItem.getSugar() +sep+
                    foodItem.getFiber() +sep+ foodItem.getProtein() +sep+ mealSize);

            printWriter.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error closing input stream: "+e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void readEntry() {


    }



}

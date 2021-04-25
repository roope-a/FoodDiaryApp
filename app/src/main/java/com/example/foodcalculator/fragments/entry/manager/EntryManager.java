package com.example.foodcalculator.fragments.entry.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EntryManager {

    private Context context;

    // TODO ask for user id
    public EntryManager(Context context) {
        this.context = context;
    }

    // TODO add user id usage
    final String CSV_NAME = "userinfo";
    final String CSV = ".csv";

    final private String sep = ";";

    //Check if we need to create a userinfo file for the user, and create it
    public void doesExist() {
        SharedPreferences preferences = context.getSharedPreferences("user",0);
        String id = preferences.getString("id", "");
        final String filepath = context.getFilesDir().getPath()+"/"+ CSV_NAME + id + CSV;

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

    //Writing a file entry
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void writeEntry(Food foodItem, double mealSize) {

        //Using the saved id to access the correct file for the user
        SharedPreferences preferences = context.getSharedPreferences("user",0);
        String id = preferences.getString("id", "");
        final String filepath = context.getFilesDir().getPath()+"/"+ CSV_NAME + id + CSV;


        File file = new File(filepath);
        PrintWriter printWriter;

        LocalDate localDate = LocalDate.now();
        String date = localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

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



    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Food> readEntries(LocalDate startDate, LocalDate endDate){

        //Using the saved id to access the correct file for the user
        SharedPreferences preferences = context.getSharedPreferences("user",0);
        String id = preferences.getString("id", "");
        final String filepath = context.getFilesDir().getPath()+"/"+ CSV_NAME + id + CSV;

        ArrayList<Food> resultList = new ArrayList<>();

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error opening input stream: "+e);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String csvLine;
            bufferedReader.readLine();
            while ((csvLine = bufferedReader.readLine()) != null) {

                //row structure date;foodName;mealType;calories;fat;sodium;carbohydrates;sugar;fiber;protein
                String row[] = csvLine.split(sep);

                LocalDate comparable = LocalDate.parse(row[0], DateTimeFormatter.ofPattern("dd.MM.yyyy"));

                //Check if entry is within the queried days
                if (comparable.compareTo(startDate) >= 0 && comparable.compareTo(endDate) <= 0) {

                    resultList.add(new Food(row[1], row[2], Double.parseDouble(row[3]), Double.parseDouble(row[4]),
                            Double.parseDouble(row[5]), Double.parseDouble(row[6]), Double.parseDouble(row[7]),
                            Double.parseDouble(row[8]), Double.parseDouble(row[9]), comparable));

                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading foods list: "+e);
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing input stream: "+e);
            }
        }
        return resultList;
    }


}

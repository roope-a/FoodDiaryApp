package com.example.foodcalculator.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodcalculator.R;
import com.example.foodcalculator.fragments.entry.manager.EntryManager;
import com.example.foodcalculator.fragments.entry.manager.Food;
import com.example.foodcalculator.fragments.entry.manager.FoodListManager;
import com.example.foodcalculator.fragments.entry.manager.FoodListPopulate;
import com.example.foodcalculator.fragments.entry.manager.Queue;
import com.example.foodcalculator.httpHandler.AsyncResponse;
import com.example.foodcalculator.httpHandler.HttpHandler;
import com.example.foodcalculator.ui.login.LoginActivity;
import com.facebook.stetho.common.StringUtil;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Saving id and username for safekeeping.
         * Allows nav menu username textfield to persist through closing the app.
         * Same for the id that's required to access the correct userinfo file.
         * */
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String id = preferences.getString("id", "");

        setContentView(R.layout.activity_main);

        EntryManager entryManager = new EntryManager(getApplicationContext());
        entryManager.doesExist();

        FoodListManager foodListManager = new FoodListManager(getApplicationContext());
//        boolean val = foodListManager.doesExist();

//        System.out.println(foodListManager.doesExist());

        if (foodListManager.doesExist()) {

            //To populate foodslist, request info using httpHandler asynchronously
            String[] tempList = getResources().getStringArray(R.array.preset);
            ConcurrentLinkedQueue<Food> queue = new ConcurrentLinkedQueue<>();

            for (String temp : tempList) {
                new HttpHandler(new AsyncResponse() {
                    @Override
                    public void processFinish(String response) {

                        //For some reason JSON didnt work with single try/catch block
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response).getJSONArray("items").getJSONObject(0);
                                String name = jsonObject.getString("name");

                                new Thread(new Queue(queue, (new Food(Character.toUpperCase(name.charAt(0)) + name.substring(1),
                                        jsonObject.getDouble("calories"),
                                        jsonObject.getDouble("fat_total_g"),
                                        (Double.parseDouble(jsonObject.getString("sodium_mg")) / 1000),
                                        jsonObject.getDouble("carbohydrates_total_g"),
                                        jsonObject.getDouble("sugar_g"),
                                        jsonObject.getDouble("fiber_g"),
                                        jsonObject.getDouble("protein_g"))))).start();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, temp);
            }
            Thread thread = new Thread (new FoodListPopulate(queue, getApplicationContext()));
            thread.start();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView username_nav = headerView.findViewById(R.id.username_here);

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        username_nav.setText(username);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_diary);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation between fragments and log out
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_diary) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_calculator) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryListFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AboutFragment()).addToBackStack(null).commit();
        } else if (id == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //Clear the remember me file so user gets prompted to login
                    SharedPreferences preferences = getSharedPreferences("rememberMe", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false").apply();

                    //Open login activity
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setMessage(R.string.log_out).create().show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Check drawer status
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void parseInputToDouble() {


    }

    // Copy paste modular solution for solving touch focus problems with text fields
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
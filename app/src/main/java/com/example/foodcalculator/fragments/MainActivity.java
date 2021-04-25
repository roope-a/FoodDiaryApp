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
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodcalculator.R;
import com.example.foodcalculator.fragments.entry.manager.EntryManager;
import com.example.foodcalculator.fragments.entry.manager.FoodListManager;
import com.example.foodcalculator.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Saving id and username for safekeeping.
        * Allows nav menu username textfield to persist through closing the app.
        * Same for the id that's required to access the correct userinfo file.
        * */
        SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String id = preferences.getString("id", "");

        setContentView(R.layout.activity_main);

        FoodListManager foodListManager = new FoodListManager(getApplicationContext());
        foodListManager.doesExist();

        EntryManager entryManager = new EntryManager(getApplicationContext());
        entryManager.doesExist();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView username_nav = headerView.findViewById(R.id.username_here);
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
        }
        else if (id == R.id.nav_diary) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryFragment()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_calculator) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DiaryListFragment()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AboutFragment()).addToBackStack(null).commit();
        }

        else if (id == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //Clear the remember me file so user gets prompted to login
                    SharedPreferences preferences = getSharedPreferences("rememberMe",MODE_PRIVATE);
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

    // Copy paste modular solution for solving touch focus problems with text fields
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
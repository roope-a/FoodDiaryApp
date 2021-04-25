package com.example.foodcalculator.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodcalculator.R;
import com.example.foodcalculator.fragments.entry.manager.EntryManager;
import com.example.foodcalculator.fragments.entry.manager.FoodListManager;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivityViewModel mainActivityViewModel;

    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        setContentView(R.layout.activity_main);

        FoodListManager foodListManager = new FoodListManager(getApplicationContext());
        foodListManager.doesExist();

        EntryManager entryManager = new EntryManager(getApplicationContext());
        entryManager.doesExist();
//        entryManager.writeEntry(new Food("Eggs Benedict", "Breakfast",
//                    ThreadLocalRandom.current().nextInt(200,400+1),  ThreadLocalRandom.current().nextInt(200,400+1),
//                    ThreadLocalRandom.current().nextInt(200,400+1), ThreadLocalRandom.current().nextInt(200,400+1),
//                    ThreadLocalRandom.current().nextInt(200,400+1), ThreadLocalRandom.current().nextInt(200,400+1),
//                    ThreadLocalRandom.current().nextInt(200,400+1)));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                    new CalculatorFragment()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AboutFragment()).addToBackStack(null).commit();
        }
        else if (id == R.id.nav_something) {
            // TODO do shit, maybe i dunno
        }
        else if (id == R.id.log_out) {
            // TODO do shit to logout

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // TODO add the logging out

                    Toast.makeText(MainActivity.this, "Hey", Toast.LENGTH_SHORT).show();

                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // TODO do we actually do anything ehre?

                }
            });

            builder.setMessage(R.string.log_out).create().show();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
package com.example.foodcalculator.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.foodcalculator.R;

public class LoadingScreen {

    Activity activity;
    AlertDialog alertDialog;

    LoadingScreen(Activity activity) {
        this.activity = activity;
    }

    void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_screen, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissLoading() {
        alertDialog.dismiss();
    }
}

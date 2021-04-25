package com.example.foodcalculator.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodcalculator.fragments.MainActivity;
import com.example.foodcalculator.R;
import com.example.foodcalculator.data.DBLogin;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        // Check to see if we resume 'previous' session with the same user
        SharedPreferences preferences = getSharedPreferences("rememberMe",MODE_PRIVATE);
        String check = preferences.getString("remember", "");
        if (check.equals("true")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final TextView registerText = findViewById(R.id.registerText);

        DBLogin db = new DBLogin(this);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                // Check to see if user has filled fields with valid info
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                //After input check if data has changed
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        //Implement listeners
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                db.open();

                // Check for user in database and see if they have an account to log into
                try {
                    if (db.Login(username, password).getBool()) {

                        // Keep the same user logged in
                        SharedPreferences preferences = getSharedPreferences("rememberMe",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "true").apply();

                        // Keep track of their id for file management
                        SharedPreferences preferences2 = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = preferences2.edit();
                        editor2.putString("username", db.Login(username, password).getUsername()).apply();
                        editor2.putString("id", db.Login(username, password).getId()).apply();

                        //Intent sends username and id to next activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        // Destroy activity
                        finish();

                    } else {
                        // Login failed message
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(R.string.login_failed).setNegativeButton(R.string.retry,null).create().show();
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });

        // Register text click
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class)
                        .putExtra("username", usernameEditText.getText().toString());
                startActivity(intent);
            }
        });

    }

}
package com.example.foodcalculator.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.text.TextUtils;
import android.util.Patterns;

import com.example.foodcalculator.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

//    public void login(String username, String password) {
//        // can be launched in a separate asynchronous job
//        Result<LoggedInUser> result = loginRepository.login(username, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
//    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password_register));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {

        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }


    private boolean isPasswordValid(String password) {

        Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[?=.*?[#?!@$%^&*-]]).{12,}$");

        return !TextUtils.isEmpty(password) && passwordPattern.matcher(password).matches();
    }
}
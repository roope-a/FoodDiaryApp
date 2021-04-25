package com.example.foodcalculator.data;

public class LoginReturn {
    private String id;
    private String username;
    private boolean bool;

    public LoginReturn(String id, String username, boolean bool) {
        this.id = id;
        this.username = username;
        this.bool = bool;
    }

    public boolean getBool() {
        return bool;
    }
    public String getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
}

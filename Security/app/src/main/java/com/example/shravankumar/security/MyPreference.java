package com.example.shravankumar.security;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Shravan Kumar on 21-04-2017.
 */

public class MyPreference {
    private static MyPreference myPreference;
    private SharedPreferences sharedPreferences;

    public static MyPreference getInstance(Context context) {
        if (myPreference == null) {
            myPreference = new MyPreference(context);
        }
        return myPreference;
    }

    private MyPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("com.example.shravankumar.security",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }
    public void saveData(String key, Boolean value){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }
    public void saveData(String key,int r){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key,r);
        prefsEditor.apply();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
    public boolean isLoggedIn(){
        String authToken = getData("Authorization");
        if(Objects.equals(authToken, "")){
            return false;
        }else return true;
    }
    public void saveAuthToken(String authToken){
        saveData("Authorization",authToken);
    }
    public String getUserName(){
        return getData("username");
    }

    public String getEmail(){
        return getData("email");
    }
    public String getMobile() {
        return getData("mobile");
    }

    public void saveUserInfo(JSONObject user){
        try {
            saveData("username",user.getString("name"));
            saveData("email",user.getString("email"));
            saveData("mobile",user.getString("mobilenumber"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void logout(){
        saveData("Authorization","");
        saveData("user_role",0);
    }

    public String getAuthToken() {
        return getData("Authorization");
    }

    public int get_role(){
        if (sharedPreferences!= null) {
            return sharedPreferences.getInt("user_role", 0);
        }
        return 0;
    }
}

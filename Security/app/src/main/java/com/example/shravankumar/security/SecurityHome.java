package com.example.shravankumar.security;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class SecurityHome extends AppCompatActivity {

    MyPreference myPreference;
    private static final String TAG="SecurityHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_home);
        myPreference = MyPreference.getInstance(getApplicationContext());
    }
    public void securityLogout(View v){
        Log.i(TAG, "Logout");
        myPreference.logout();
        Intent in= new Intent(this, MainActivity.class);
        startActivity(in);
    }
    public void Validate(View v){
        Intent i= new Intent(this, ValidateVisitor.class);
        startActivity(i);
    }
    public void edit_security_profile(View v){
        Intent i = new Intent(this, SecurityEdit.class);
        startActivity(i);
    }
    public void enterotp(View v){
        Intent i = new Intent(this, EnterOTP.class);
        startActivity(i);
    }
}

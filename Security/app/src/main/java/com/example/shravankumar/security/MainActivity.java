package com.example.shravankumar.security;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MyPreference yourPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void main_login(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void main_register(View v){
        startActivity(new Intent(this,RegisterActivity.class));
    }
    public void security_register(View v){
        Intent i = new Intent(this, SecurityRegister.class);
        startActivity(i);
    }
}

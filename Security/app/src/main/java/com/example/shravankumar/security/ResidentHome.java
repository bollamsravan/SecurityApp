package com.example.shravankumar.security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResidentHome extends BaseActivity {

    MyPreference myPreference;
    private SharedPreferences preferences;
    private static final String TAG="ResidentHome";
//    @Bind(R.id.welcome_person) TextView text;
    @Bind(R.id.toggleButton) ToggleButton tg;
    @Bind(R.id.notifications)
    Button _notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_home);
        myPreference = MyPreference.getInstance(getApplicationContext());
        ButterKnife.bind(this);
        getAvailability();
//        String username = "UserName";
//        text.setText(username);
        tg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick_toggle");
                    checkedTrue();
            }
        });
        _notify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick_notify");
                Notify();
            }

        });

    }

    private void Notify(){
        Intent i= new Intent(this, ResidentNotifications.class);
        startActivity(i);
    }

    private void getAvailability() {
        final ProgressDialog progressDialog = new ProgressDialog(ResidentHome.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("getting Availability...");
        progressDialog.show();
        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {

                    if (response.getInt("status") == Constants.STATUS_OK) {
//                        createShortToast("getting availability success");
                        tg.setChecked(response.getBoolean("availability"));
                    }else {
//                        createShortToast("setting availability failed");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MyRequest objRequest = new MyRequest(Request.Method.POST,Routes.ResidentAvailable,null,responseListner,null,getApplicationContext());
        sendRequest(objRequest);

    }


    public void checkedTrue(){
        Log.i(TAG, "checkedTrue");

        final ProgressDialog progressDialog = new ProgressDialog(ResidentHome.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Setting Availability...");
        progressDialog.show();

        Log.v(TAG,"TO create a request");
        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("response",response.toString());
                try {
                    if(response.getInt("status") == Constants.STATUS_BAD_REQUEST){
//                        onAddHotelFailed(response);
                        createShortToast("setting availability failed");
                    }else if (response.getInt("status") == Constants.STATUS_OK) {
//                        onAddHotelSuccess(response);
                        createShortToast("setting availability success");
//                        onAvailableSuccess(response);
                    }else {
//                        onAddHotelFailed(response);
                        createShortToast("setting availability failed");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JSONObject availableObject = createAvailableObject();
        MyRequest availableRequest = new MyRequest(Request.Method.POST, Routes.Availability, availableObject, responseListner,null,getApplicationContext());
        sendRequest(availableRequest);
    }

//    public void onAvailableSuccess(JSONObject response) {
//        Boolean availableObj = response.getBoolean("availability");
//    }

    private JSONObject createAvailableObject(){
        JSONObject availableObject = new JSONObject();
        Boolean is_checked=tg.isChecked();
        try {
            availableObject.put("availability",is_checked);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("available object",availableObject.toString());
        return availableObject;
    }

    public void edit(View v){
        Intent i= new Intent(this,EditProfile.class);
        startActivity(i);
    }

    public void Logout(View v){
        Log.i(TAG, "Logout");
        myPreference.logout();
        Intent in= new Intent(this, MainActivity.class);
        startActivity(in);
    }
}

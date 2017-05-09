package com.example.shravankumar.security;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    MyPreference myPreference;

    @Bind(R.id.login_email) EditText _emailText;
    @Bind(R.id.login_password) EditText _passwordText;
    @Bind(R.id.email_sign_in_button) Button _loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myPreference = MyPreference.getInstance(getApplicationContext());
        checkLoggedIn();
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

//        _signupLink.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Start the Signup activity
//                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(null);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        JSONObject signInObject = getSignObject(email,password);

        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                Log.i("SignIn", response.toString());

                try {
                    if(response.getInt("status") == Constants.STATUS_UNAUTHORIZED){
                        onLoginFailed(response);
                    }else if (response.getInt("status") == Constants.STATUS_OK) {
                        onLoginSuccess(response);
                    }else {
                        onLoginFailed(response);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Log.d(TAG,"Creating a request");
        JsonObjectRequest signInRequest = new JsonObjectRequest(Request.Method.POST,Routes.Authenticate,signInObject,responseListner,null);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(signInRequest);
    }
    @Override
    public void onBackPressed() {
        if(!myPreference.isLoggedIn()) {
            moveTaskToBack(true);
        }
    }

    private JSONObject getSignObject(String email, String password) {
        Log.d("getSignObjectfunc",email+password);
        JSONObject signInObject = new JSONObject();
        try {
            signInObject.put("email",email);
            signInObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return signInObject;
    }


    public void onLoginSuccess(JSONObject response) throws JSONException {

        Toast.makeText(LoginActivity.this, "LoggedIN", Toast.LENGTH_SHORT).show();

        String authToken = response.getString("auth_token");
        myPreference.saveAuthToken(authToken);
        Log.v("On Login Success",response.toString());
        JSONObject user = response.getJSONObject("user");
        myPreference.saveUserInfo(user);
        int role  = user.getInt("role");
        if (role == Constants.ROLE_ADMIN){
            //TODO admin activity
        }
        else if(role == Constants.ROLE_RESIDENT){
            myPreference.saveData("user_role",role);
            Intent intent = new Intent(this, ResidentHome.class);
            startActivity(intent);
        }
        else if(role == Constants.ROLE_SECURITY){
            myPreference.saveData("user_role",role);
            Intent intent = new Intent(this, SecurityHome.class);
            startActivity(intent);
        }
        finish();
    }

    public void onLoginFailed(JSONObject response) {
        Toast.makeText(getBaseContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
        if (response != null){
            Log.d(TAG,response.toString());
        }
        else
            Log.d(TAG,"Response is null");
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void checkLoggedIn() {
        if(myPreference.isLoggedIn()){
            int role=myPreference.get_role();
            if(role==Constants.ROLE_RESIDENT){
                startResidentActivity();
            }
            else if(role==Constants.ROLE_SECURITY){
                startSecurityActivity();
            }
        }
    }

    private void startResidentActivity() {
        Intent intent = new Intent(this,ResidentHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LoginActivity.this.startActivity(intent);
    }
    private void startSecurityActivity() {
        Intent intent = new Intent(this,SecurityHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LoginActivity.this.startActivity(intent);
    }



}

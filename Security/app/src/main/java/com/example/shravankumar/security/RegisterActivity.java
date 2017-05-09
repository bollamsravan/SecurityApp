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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @Bind(R.id.register_name) EditText _nameText;
    @Bind(R.id.register_email) EditText _emailText;
    @Bind(R.id.mobile) EditText _mobileText;
    @Bind(R.id.register_password) EditText _passwordText;
    @Bind(R.id.confirm_password) EditText _reEnterPasswordText;
    @Bind(R.id.door_no) EditText _doorNo;
    @Bind(R.id.residence) EditText _residence;
    @Bind(R.id.resident_register) Button _signupButton;
    MyPreference myPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        myPreference = MyPreference.getInstance(getApplicationContext());

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_signup();
            }
        });



//        _loginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Finish the registration screen and return to the Login activity
//                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
//        });
    }
    public void register_signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onRegisterFailed(null);
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String door_num = _doorNo.getText().toString();
        String residence_res=_residence.getText().toString();

        // TODO: Implement your own signup logic here.
        JSONObject signupObject = createSignupObject(name,email,mobile,password,reEnterPassword,door_num,residence_res);

        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.v("on response",response.toString());
                try {
                    if(response.getInt("status") == Constants.STATUS_BAD_REQUEST){
                        onRegisterFailed(response);
                    }else if (response.getInt("status") == Constants.STATUS_CREATED) {
                        onRegisterSuccess(response);
                    }else {
                        onRegisterFailed(response);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        JsonObjectRequest registerRequest = new JsonObjectRequest(Routes.Register,signupObject,responseListner,null);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);


    }



    private void onRegisterSuccess(JSONObject response) {
        try {
            JSONObject userObj = response.getJSONObject("resident").getJSONObject("user");
            String authToken = response.getString("auth_token");
//            myPreference.saveAuthToken(authToken);
            myPreference.saveUserInfo(userObj);
            Toast.makeText(getBaseContext(),"RegisterSuccess", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        RegisterActivity.this.startActivity(intent);
    }

    private void onRegisterFailed(JSONObject response) {
        Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_SHORT).show();
        if (response != null) {
            Log.d(TAG, response.toString());
            try {
                if(response.getInt("status")==Constants.STATUS_BAD_REQUEST){
                    String errorMsg = response.getJSONObject("errors").getJSONArray("email").getString(0);
                    Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            Log.d(TAG,"Response is null");
    }

    private JSONObject createSignupObject(String name, String email, String mobile, String password, String reEnterPassword, String door_num, String residence_res) {
        JSONObject signupObj = new JSONObject();
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("email",email);
            userObj.put("name",name);
            userObj.put("password",password);
            userObj.put("mobilenumber",mobile);
            userObj.put("password_confirmation",reEnterPassword);
            signupObj.put("user",userObj);
            signupObj.put("door_no",door_num);
            signupObj.put("residence",residence_res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return signupObj;
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String door_num = _doorNo.getText().toString();
        String residence_res=_residence.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }
        if (door_num.isEmpty()||door_num.length() < 2 ||  door_num.length() > 6 ) {
            _doorNo.setError("Enter valid door number");
            valid=false;
        }else{
            _doorNo.setError(null);
        }
        if (residence_res.isEmpty()||residence_res.length() < 2 ||  residence_res.length() > 30 ) {
            _residence.setError("Enter valid door number");
            valid=false;
        }else{
            _residence.setError(null);
        }

        return valid;
    }

}

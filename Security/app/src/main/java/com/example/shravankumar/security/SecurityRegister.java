package com.example.shravankumar.security;

import android.app.ProgressDialog;
import android.os.Bundle;
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

import static com.example.shravankumar.security.Routes.SecurityRegister;

public class SecurityRegister extends BaseActivity {
    private static final String TAG="SecurityRegister";

    @Bind(R.id.security_name)
    EditText _securityName;
    @Bind(R.id.security_email) EditText _securityEmail;
    @Bind(R.id.security_phone) EditText _securityPhone;
    @Bind(R.id.security_password) EditText _securityPassword;
    @Bind(R.id.confirm_sec_password) EditText _confirmPassword;
    @Bind(R.id.security_register)
    Button _securityRegisterbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_register);
        myPreference = MyPreference.getInstance(getApplicationContext());
        ButterKnife.bind(this);
        _securityRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securityRegistration();
            }
        });
    }

    private void securityRegistration(){
        Log.i(TAG, "securityRegistration");
        if(!validate()){
            onRegisterFailed(null);
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(SecurityRegister.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating....");
        progressDialog.show();

        String name = _securityName.getText().toString();
        String email = _securityEmail.getText().toString();
        String mobile = _securityPhone.getText().toString();
        String password = _securityPassword.getText().toString();
        String reEnterPassword = _confirmPassword.getText().toString();

        JSONObject signupObject = createSignupObject(name,email,mobile,password,reEnterPassword);

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

        JsonObjectRequest registerRequest = new JsonObjectRequest(SecurityRegister,signupObject,responseListner,null);
        RequestQueue queue = Volley.newRequestQueue(SecurityRegister.this);
        queue.add(registerRequest);

    }
    private void onRegisterSuccess(JSONObject response) {
        try {
            JSONObject userObj = response.getJSONObject("security").getJSONObject("user");
            String authToken = response.getString("auth_token");
//            myPreference.saveAuthToken(authToken);
            myPreference.saveUserInfo(userObj);
            Toast.makeText(getBaseContext(),"RegisterSuccess", Toast.LENGTH_SHORT).show();

            startMainActivity();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void startMainActivity() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        SecurityRegister.this.startActivity(intent);
//    }

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

    private JSONObject createSignupObject(String name, String email, String mobile, String password, String reEnterPassword) {
        JSONObject signupObj = new JSONObject();
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("email",email);
            userObj.put("name",name);
            userObj.put("password",password);
            userObj.put("mobilenumber",mobile);
            userObj.put("password_confirmation",reEnterPassword);
            signupObj.put("user",userObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return signupObj;
    }

    public boolean validate() {
        boolean valid = true;

        String name = _securityName.getText().toString();
        String email = _securityEmail.getText().toString();
        String mobile = _securityPhone.getText().toString();
        String password = _securityPassword.getText().toString();
        String reEnterPassword = _confirmPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _securityName.setError("at least 3 characters");
            valid = false;
        } else {
            _securityName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _securityEmail.setError("enter a valid email address");
            valid = false;
        } else {
            _securityEmail.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _securityPhone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _securityPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _securityPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _securityPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _confirmPassword.setError("Password Do not match");
            valid = false;
        } else {
            _confirmPassword.setError(null);
        }
        return valid;
    }

}

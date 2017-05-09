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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditProfile extends AppCompatActivity {
    private RequestQueue queue;
    @Bind(R.id.et_name) EditText _name;
    @Bind(R.id.et_mobileNumber) EditText _mobileNumber;
    @Bind(R.id.et_password)  EditText _password;
    @Bind(R.id.et_email) EditText _email;
    @Bind(R.id.btn_update)
    Button _update;

    private static final String TAG = EditProfile.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        MyPreference myPreference = MyPreference.getInstance(getApplicationContext());
        ButterKnife.bind(this);

        String name= myPreference.getUserName();
        String email = myPreference.getEmail();
        String mobileNo = myPreference.getMobile();

        _name.setText(name);
        _email.setText(email);
        Log.i(mobileNo, "onCreate: ");
        _mobileNumber.setText(mobileNo);

        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }
    private void update() {
        Log.d(TAG, "update");

        if (!validate()) {
            onUpdateFailed(null);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating....");
        progressDialog.show();

        String name = _name.getText().toString();
        String password = _password.getText().toString();
        String mobileNumber = _mobileNumber.getText().toString();
        String email = _email.getText().toString();

        JSONObject updateObject = createUpdateObject(name,password,mobileNumber,email);

        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){

            /**
             * Called when a response is received.
             *
             * @param response
             */
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt("status") == Constants.STATUS_BAD_REQUEST){
                        onUpdateFailed(response);
                    }else if (response.getInt("status") == Constants.STATUS_OK) {
                        onUpdateSuccess(response);
                    }else {
                        onUpdateFailed(response);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyRequest updateRequest = new MyRequest(Request.Method.POST, Routes.Update, updateObject,
                responseListner, null, getApplicationContext());
        sendRequest(updateRequest);
    }
    protected void createShortToast(String content){
        Toast.makeText(getBaseContext(), content, Toast.LENGTH_SHORT).show();
    }

    protected void createLongToast(String contest){
        Toast.makeText(getBaseContext(), contest, Toast.LENGTH_LONG).show();
    }
    protected void sendRequest(MyRequest req){
        if(queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }else {
            queue.add(req);
        }
    }

    private void onUpdateSuccess(JSONObject response) {
        Log.i(TAG, "On Update Success");
        Log.d(TAG,"Respnse: " + response.toString());
        Toast.makeText(getBaseContext(),"Updated successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ResidentHome.class);
        startActivity(intent);
        finish();
    }

    /**
     * Creates a Update object that should be sent to server
     * @param name email that should be sent
     * @param password  password that should be sent
     * @param mobileNumber the mobile number that should be updated
     * @return update json object if in valid json format else returns null
     */
    private JSONObject createUpdateObject(String name, String password, String mobileNumber, String email) {
        JSONObject updateObject = new JSONObject();
        JSONObject userObject = new JSONObject();
        try {
            userObject.put("name",name);
            userObject.put("password",password);
            userObject.put("mobilenumber",mobileNumber);
            userObject.put("email",email);
            updateObject.put("user",userObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return updateObject;
    }

    private void onUpdateFailed(JSONObject response) {
        Log.i(TAG,"IN Update Failed");
//        createShortToast("Update Failed");
        if(response != null){
            Log.d(TAG,response.toString());
        }else{
            Log.d(TAG,"response is null");
        }
    }


    /**
     * Validates the Edit text fields and checks if they contain valid content so we can create a
     * valid request
     * Sets Error over the Edit text fields if they contain non valid content
     * @return true if valid else false
     * @see EditText#setError(CharSequence)
     */
    public boolean validate() {
        Log.i(TAG,"IN Validate");
        boolean valid = true;

        String name = _name.getText().toString();
        String password = _password.getText().toString();
        String mobileNumber = _mobileNumber.getText().toString();
        String email = _email.getText().toString();

        if (name.isEmpty()) {
            Log.i(TAG,"Name is Empty");
            _name.setError("can Not be Empty");
            valid = false;
        } else {
            _name.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _password.setError("should be a min of 8 characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        if (mobileNumber.isEmpty() || mobileNumber.length()!=10) {
            _mobileNumber.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileNumber.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        return valid;
    }
}

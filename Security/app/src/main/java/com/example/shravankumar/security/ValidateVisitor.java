package com.example.shravankumar.security;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ValidateVisitor extends BaseActivity {

    private static final String TAG = "ValidateVisitor";

    @Bind(R.id.details_name) EditText visitorName;
    @Bind(R.id.details_phone) EditText visitorPhone;
    @Bind(R.id.details_address) EditText visitorAddress;
    @Bind(R.id.details_resident) EditText residentName;
    @Bind(R.id.door_number) EditText doorNumber;
    @Bind(R.id.send_request) Button send_req_btn;
    @Bind(R.id.purpose) EditText _purpose;
    @Bind(R.id.security_back) Button _checkAvailable;
    @Bind(R.id.yes_no_textview)TextView _yes_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_visitor);
        final MyPreference myPreference = MyPreference.getInstance(getApplicationContext());
        ButterKnife.bind(this);

        send_req_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateVisitor();
                Log.i(TAG, "onClick:validatevisitor ");
            }
        });
        _checkAvailable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                check_available();
                Log.i(TAG, "onClick:check_available ");
            }
        });
    }
    public void validateVisitor(){
        Log.d(TAG, "validateVisitor");
        if (!validate()) {
//            onValidateFailed(null);
            Log.i(TAG, "validateVisitor:validateFailed");
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(ValidateVisitor.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Request....");
        progressDialog.show();

        String name = visitorName.getText().toString();
        String phone= visitorPhone.getText().toString();
        String address = visitorAddress.getText().toString();
        String resident_name= residentName.getText().toString();
        String door_no = doorNumber.getText().toString();
        String purpose = _purpose.getText().toString();

        JSONObject validateObject = createValidateObject(name,phone,address,resident_name,door_no,purpose);

        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.v("on response",response.toString());
                try {
                    if(response.getInt("status") == Constants.STATUS_BAD_REQUEST){
//                        onValidateFailed(response);
                        Log.i(TAG, "validate:Failed ");
                        createShortToast("Visitor Creation Failed");
                    }else if (response.getInt("status") == Constants.STATUS_CREATED) {
                        onValidateSuccess(response);
                        Log.i(TAG, "Validate:Success");
                        createShortToast("Visitor Created");
                    }else {
//                        onValidateFailed(response);
                        Log.i(TAG, "validate:Failed ");
                        createShortToast("Visitor creation Failed");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyRequest validateRequest = new MyRequest(Request.Method.POST,Routes.Validate,validateObject,responseListner,null,getApplicationContext());
        RequestQueue queue = Volley.newRequestQueue(ValidateVisitor.this);
        queue.add(validateRequest);

    }

    public void check_available(){
        Log.d(TAG, "check_available");
        final ProgressDialog progressDialog = new ProgressDialog(ValidateVisitor.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Checking availability....");
        progressDialog.show();
        String door_no = doorNumber.getText().toString();
        JSONObject availableObject = createAvailableObject(door_no);

        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.v("on response",response.toString());
                try {
                    if (response.getInt("status") == Constants.STATUS_OK) {
                        onAvailableSuccess(response);
                        Log.i(TAG, "available:Success");
                        createShortToast("Available request success");
                    }else {
//                        onValidateFailed(response);
                        Log.i(TAG, "Available:Failed ");
                        createShortToast("Visitor request failed");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        MyRequest availableRequest = new MyRequest(Request.Method.POST,Routes.CheckAvailable, availableObject,responseListner,null,getApplicationContext());
//        RequestQueue queue = Volley.newRequestQueue(ValidateVisitor.this);
//        queue.add(availableRequest);
        sendRequest(availableRequest);
    }

    private void onAvailableSuccess(JSONObject response){
            try {
                Boolean availObj= response.getBoolean("availability");
                if(availObj){
                    createShortToast("Resident available");
                    _yes_no.setText("yes");
                }
                else {
                    createShortToast("Resident not available");
                    _yes_no.setText("No");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    private JSONObject createAvailableObject(String door_no){
        JSONObject availableObj = new JSONObject();
        try{
            availableObj.put("door_no",door_no);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return availableObj;
    }

    private void onValidateSuccess(JSONObject response) {
        try {
            JSONObject userObj = response.getJSONObject("resident").getJSONObject("user");
            String authToken = response.getString("auth_token");
//            myPreference.saveAuthToken(authToken);
            myPreference.saveUserInfo(userObj);
            Toast.makeText(getBaseContext(),"Validate", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createValidateObject(String name, String phone, String address, String resident_name, String door_no, String purpose) {
        JSONObject validateObj = new JSONObject();
        JSONObject userObj = new JSONObject();
        try {
            userObj.put("name",name);
            userObj.put("phone",phone);
            userObj.put("address",address);
            userObj.put("resident_name",resident_name);
            userObj.put("door_no",door_no);
            userObj.put("purpose",purpose);
//            validateObj.put("door_no",door_num);
//            validateObj.put("residence",residence_res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userObj;
    }

    public boolean validate(){
        boolean valid = true;

        String name = visitorName.getText().toString();
        String phone= visitorPhone.getText().toString();
        String address = visitorAddress.getText().toString();
        String resident_name= residentName.getText().toString();
        String door_no = doorNumber.getText().toString();
        String purpose = _purpose.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            visitorName.setError("at least 3 characters");
            valid = false;
        } else {
            visitorName.setError(null);
        }


        if (phone.isEmpty() || phone.length()!=10) {
            visitorPhone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            visitorPhone.setError(null);
        }

        if (address.isEmpty() || address.length() < 4 || address.length() > 40) {
            visitorAddress.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            visitorAddress.setError(null);
        }

        if (resident_name.isEmpty() || resident_name.length() < 3) {
            residentName.setError("at least 3 characters");
            valid = false;
        } else {
            residentName.setError(null);
        }

        if (door_no.isEmpty()||door_no.length() < 2 ||  door_no.length() > 6 ) {
            doorNumber.setError("Enter valid door number");
            valid=false;
        }else{
            doorNumber.setError(null);
        }
        if (purpose.isEmpty()||purpose.length() < 1 || purpose.length() > 30 ) {
            _purpose.setError("Enter valid door number");
            valid=false;
        }else{
            _purpose.setError(null);
        }



        return valid;
    }

}

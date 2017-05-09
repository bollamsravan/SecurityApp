package com.example.shravankumar.security;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.shravankumar.security.R.id.phone;

public class ResidentNotifications extends BaseActivity {

    MyPreference myPreference;
    private static final String TAG="ResidentNotifications";
    @Bind(R.id.person_name) TextView _visitorName;
    @Bind(phone) TextView _visitorPhone;
    @Bind(R.id.address) TextView _visitorAddress;
    @Bind(R.id.accept) Button _acceptbtn;
    @Bind(R.id.reject) Button _rejectbtn;
    @Bind(R.id.exit_visitor)Button _exitVisitorbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_notifications);
        myPreference=MyPreference.getInstance(getApplicationContext());
        ButterKnife.bind(this);
        Display();


//        _acceptbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

    }

    private void Display(){
        Log.i(TAG, "Display:");

        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.v("on response",response.toString());
                try {
                    if (response.getInt("status") == Constants.STATUS_OK) {
//                        onAvailableSuccess(response);
                        onGetNotify(response);
                        Log.i(TAG, "get_notification:Success");
//                        createShortToast("Available request success");
                    }else {
//                        onValidateFailed(response);
                        Log.i(TAG, "get_notification:Failed ");
//                        createShortToast("Visitor request failed");
                    }
//                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MyRequest objRequest = new MyRequest(Request.Method.POST,Routes.GetNotifications, null, responseListner,null,getApplicationContext());
        sendRequest(objRequest);

    }
    private void onGetNotify(JSONObject response){
        try {
            JSONArray visitors = response.getJSONArray("visitors");
            JSONObject visitor = visitors.getJSONObject(0);
            _visitorName.setText("VisitorName:"+visitor.getString("name"));
            _visitorPhone.setText("VisitorMobile:"+visitor.getString("phone"));
            _visitorAddress.setText("VisitorAddress:"+visitor.getString("address"));
            final int visitor_id= visitor.getInt("id");
            final String visitor_phone = visitor.getString("phone");
            _acceptbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Accept(visitor_id,visitor_phone);
                }
            });
            _rejectbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reject(visitor_id);
                }

            });
            _exitVisitorbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit(visitor_phone);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void exit(final String Phone){

    }

    private void Accept(int a,final String phone){
        Log.i(TAG, "Accept: ");
        final ProgressDialog progressDialog = new ProgressDialog(ResidentNotifications.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Accepting Request....");
        progressDialog.show();
        JSONObject AcceptObj = createAcceptObj(a,true);
        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.v("on response",response.toString());
                try {
                    if(response.getInt("status") == Constants.STATUS_BAD_REQUEST){
                        createShortToast("Accept Failed");
                    }
                    else if (response.getInt("status") == Constants.STATUS_OK) {

//                        onGetNotify(response);
                        Log.i(TAG, "AcceptorReject:Success");
                        final String message="Your OTP is : 731248";
//                        sendSMS(phone, message);
//                        createShortToast("Accept Success");


                    }else {
//                        onValidateFailed(response);
                        Log.i(TAG, "AcceptorReject:Failed ");
//                        createShortToast("Visitor request failed");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MyRequest objRequest = new MyRequest(Request.Method.POST,Routes.AcceptReject, AcceptObj, responseListner,null,getApplicationContext());
        sendRequest(objRequest);
    }
//    private void sendSMS(String phoneNumber, String msg) {
//        Log.i(TAG, "sendSMS:");
//         final String ACCOUNT_SID = "AC4f44f1ba4b94da0d27ed11e19f3ab84c";
//         String AUTH_TOKEN = "34ffe96833a8ba28bc901b4e10c16e1c";
//
//       TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
//
//            // Build the parameters
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("To", phoneNumber));
//            params.add(new BasicNameValuePair("From", "+16364220901"));
//            params.add(new BasicNameValuePair("Body", msg));
//
//        MessageFactory messageFactory = client.getAccount().getMessageFactory();
//        Message message = null;
//        try {
//        } catch (TwilioRestException e) {
//            e.printStackTrace();
//        }
//        System.out.println(message.getSid());
//    }

    private void Reject(int a){
        Log.i(TAG, "Reject:");
        final ProgressDialog progressDialog = new ProgressDialog(ResidentNotifications.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Reject Request....");
        progressDialog.show();
        JSONObject AcceptObj = createRejectObj(a,false);
        Response.Listener<JSONObject> responseListner = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.v("on response",response.toString());
                try {
                    if(response.getInt("status") == Constants.STATUS_BAD_REQUEST){
                        createShortToast("Reject Failed");
                    }
                    else if (response.getInt("status") == Constants.STATUS_OK) {

//                        onGetNotify(response);
                        Log.i(TAG, "AcceptorReject:Success");
                        createShortToast("Reject Success");
                    }else {
//                        onValidateFailed(response);
                        Log.i(TAG, "Reject:Failed ");
//                        createShortToast("Visitor request failed");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MyRequest objRequest = new MyRequest(Request.Method.POST,Routes.AcceptReject, AcceptObj, responseListner,null,getApplicationContext());
        sendRequest(objRequest);

    }

    private JSONObject createAcceptObj(int a,Boolean b){
        JSONObject AccObj = new JSONObject();
        try {
            AccObj.put("visitor_id",a);
            AccObj.put("acceptance",b);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AccObj;

    }
    private JSONObject createRejectObj(int a,Boolean b){
        JSONObject RejObj = new JSONObject();
        try {
            RejObj.put("visitor_id",a);
            RejObj.put("acceptance",b);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RejObj;

    }





}

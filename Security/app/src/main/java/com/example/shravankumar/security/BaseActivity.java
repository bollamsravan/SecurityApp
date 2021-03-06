package com.example.shravankumar.security;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Shravan Kumar on 24-04-2017.
 */

public class BaseActivity extends AppCompatActivity{
    protected MyPreference myPreference;
    private static final String TAG = BaseActivity.class.getName();

    private RequestQueue queue;
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.actions, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        Log.i(TAG,"menu item clicked");
//
//        if (id == R.id.action_my_account) {
//            startMyAccountActivity();
//        } else if(id == R.id.action_logout) {
//            Log.i("Menu Item","logout");
//            myPreference.logout();
//            startLoginActivity();
//        } else if(id == R.id.action_login){
//            startLoginActivity();
//        } else if(id == R.id.action_admin){
//            startAdminActivity();
//        } else if(id == R.id.action_manager){
//            startManagerActivity();
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }

    protected void startLoginActivity(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

//    protected void startMyAccountActivity(){
//        Intent intent = new Intent(getApplicationContext(),MyAccountActivity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
//    }

    protected void startMainActivity(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
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
            queue.add(req);
        }else {
            queue.add(req);
        }
    }

//    void startAdminActivity() {
//        Intent intent = new Intent(getApplicationContext(),AdminActivity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
//    }
    void startManagerActivity(){
//        TODO create a intent to start the manager Activity
    }
}

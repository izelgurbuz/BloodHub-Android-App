package com.bloodhub.android.activities;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.model.Notification;
import com.bloodhub.android.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by izelgurbuz on 28.02.2018.
 */

public class EmergencyFiveTransactionDesicisionActivity extends AppCompatActivity {

    EditText nameText, statusText, emailText;
    String status ;
    String fullName;
    int userID;
    int ownerID;
    String classname ;
    String email;

    EmergencyFiveTransactionDesicisionActivity thisclass = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyfivetransactiondecision);
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Bundle b = getIntent().getExtras();
        status = b.getString("statusstr");
        fullName = b.getString("fullName");
        email = b.getString("email");
        userID = b.getInt("userID");
        ownerID = b.getInt("ownerID");
        classname = b.getString("class");



        nameText = (EditText) findViewById(R.id.transactionXMLname);
        emailText = (EditText) findViewById(R.id.transactionXMLemail);
        statusText = (EditText) findViewById(R.id.transactionXMLstatus);

        nameText.setText(fullName);
        emailText.setText(email);
        statusText.setText(status);

        Button transactionXMLbuttonReject=(Button)findViewById(R.id.transactionXMLbuttonReject);
        if(status.equals("Confirmed")){
            ((Button)(findViewById(R.id.transactionXMLbuttonConfirm))).setVisibility(View.INVISIBLE);
            ((Button)(findViewById(R.id.transactionXMLbuttonReject))).setVisibility(View.VISIBLE);

        }
        else if(status.equals("Rejected")){
            ((Button)(findViewById(R.id.transactionXMLbuttonReject))).setVisibility(View.INVISIBLE);
            ((Button)(findViewById(R.id.transactionXMLbuttonConfirm))).setVisibility(View.VISIBLE);


        }
        else{
            ((Button)(findViewById(R.id.transactionXMLbuttonConfirm))).setVisibility(View.VISIBLE);
            ((Button)(findViewById(R.id.transactionXMLbuttonReject))).setVisibility(View.VISIBLE);

        }

        findViewById(R.id.transactionXMLbuttonReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTransactionChoice("-1");


            }
        });
        findViewById(R.id.transactionXMLbuttonConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTransactionChoice("1");



            }
        });




    }

    private void sendTransactionChoice(String r) {

        final int response = r == "1" ? 1: -1;

        final int yourIDInner = userID;
        final int ownerIDInner= ownerID;

        class notificationResponse extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.transactionXMLprogressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                // s = "{" + s + "}";
                super.onPostExecute(s);
                progressBar.setVisibility(View.GONE);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    if (obj.getString("error").equals("FALSE")) {

                        Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), EmergencyFiveTransactionActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("choice", response+"");
                params.put("ownerID", yourIDInner+"");
                params.put("yourID", ownerIDInner+"");

                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_approvePersonalEM5ListRequest, params);

            }
        }

        notificationResponse ul = new notificationResponse();
        ul.execute();


    }
}

package com.bloodhub.android.activities;
import android.app.ActionBar;
import android.support.v4.content.res.ResourcesCompat;
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

public class notificationResponseActivity extends AppCompatActivity {

    TextView title, msg;
    int patientID;
    int bloodRequestID;
    String msgstr;
    String titlestr;
    User user;
    String classname ;
    double latitude, longitude;
    String placeName;
    LinearLayout linearresponse;

    notificationResponseActivity thisclass = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationresponse);
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        Bundle b = getIntent().getExtras();
        msgstr = b.getString("msgstr");
        titlestr = b.getString("titlestr");
        patientID = b.getInt("patientID");
        bloodRequestID = b.getInt("bloodRequestID");
        latitude = b.getDouble("latitude");
        longitude = b.getDouble("longitude");
        classname = b.getString("class");
        placeName = b.getString("placeName");

        Log.e("titlestr ", titlestr);

        user = SharedPreferencesManager.getInstance(this).getUser();

        title = (TextView) findViewById(R.id.title);
        msg = (TextView) findViewById(R.id.msg);
        linearresponse =(LinearLayout)findViewById(R.id.linearResponse);
        linearresponse.setBackgroundResource(R.drawable.my_fullframe_layout);
        LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        textparams.setMargins(10,20,10,10);
        title.setLayoutParams(textparams);
        msg.setLayoutParams(textparams);
        linearresponse.setLayoutParams(textparams);

        title.setText(titlestr);
        title.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgray, null));

        msg.setText(msgstr);
        msg.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgray, null));

        if(classname.equals("r")){
            findViewById(R.id.buttonReject).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendNotificationResponse(-1);


                }
            });
            findViewById(R.id.buttonConfirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendNotificationResponse(1);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitude", latitude);
                    bundle.putDouble("longitude", longitude);
                    bundle.putString("placeName", placeName);
                    Intent i = new Intent(thisclass, BloodRequestMapPointActivity.class);
                    i.putExtras(bundle);
                    //i.putExtra("titlestr", titlestr);
                    startActivity(i);
                    finish();


                }
            });

        }
        else if(classname.equals("s")){
            LinearLayout layout = findViewById(R.id.linearButton);
            layout.setVisibility(View.GONE);
            Button bu = (Button) findViewById(R.id.buttonReject);
            bu.setVisibility(View.GONE);
            findViewById(R.id.buttonConfirm).setVisibility(View.GONE);


        }

    }

    private void sendNotificationResponse(int r) {

        final int response = r;
        final int fpatientID = patientID;
        final int fdonorID= user.getID();
        final int fbloodRequestID= bloodRequestID;
        class notificationResponse extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                    Log.e("JSONOBJEsi:  ", obj.toString(4));
                    //if no error in response

                    Log.e("JSONERROR:  ", obj.getString("error"));

                    if (obj.getString("error").equals("FALSE")) {
                        //JSONObject userInstance = obj.getJSONObject("user");


                        Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                        if(response == -1)
                            finish();
                        //finish();
                        //startActivity(new Intent(getApplicationContext(), myReceivedNotificationActivity.class));
                        //getting the user from the response


                        //creating a new user object
                        /*User user = new User(
                                userInstance.getInt("id"),
                                userInstance.getString("username"),
                                userInstance.getString("email")

                        );
                        Log.e("USERNAME:  ", ""+user.getUsername());
                        //storing the user in shared preferences
                        SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(user);
                        */
                        //starting the profile activity
                        //
                        //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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
                params.put("patientID", fpatientID+"");
                params.put("donorID", fdonorID+"");
                params.put("bloodRequestID", fbloodRequestID+"");



                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_saveReceiverCondition, params);

            }
        }

        notificationResponse ul = new notificationResponse();
        ul.execute();


    }
}
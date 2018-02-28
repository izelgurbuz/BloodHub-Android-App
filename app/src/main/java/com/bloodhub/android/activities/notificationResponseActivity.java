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

import static com.bloodhub.android.R.id.editTextnameSurname;
import static com.bloodhub.android.R.id.textView;

/**
 * Created by izelgurbuz on 28.02.2018.
 */

public class notificationResponseActivity extends AppCompatActivity {

    EditText title, msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationresponse);

        Bundle b = getIntent().getExtras();
        String msgstr = b.getString("msgstr");
        String titlestr = b.getString("titlestr");
        Log.e("titlestr ", titlestr);

        title = (EditText) findViewById(R.id.title);
        msg = (EditText) findViewById(R.id.msg);
        title.setText(titlestr);
        msg.setText(msgstr);
        findViewById(R.id.buttonReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotificationResponse(0);


            }
        });
        findViewById(R.id.buttonConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotificationResponse(1);


            }
        });



    }

    private void sendNotificationResponse(int r) {

        int response = r;
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


                        //Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                        //finish();
                        //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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

                //params.put("name_surname", nameSurname);


                //returing the response
                //return requestHandler.sendPostRequest(Constants.URL_sendBloodRequest, params);
                return "";
            }
        }

        notificationResponse ul = new notificationResponse();
        ul.execute();


    }
}
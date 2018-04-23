package com.bloodhub.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.app.Config;
import com.bloodhub.android.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by izelgurbuz on 3.02.2018.
 */

public class ProfileActivity extends BaseActivity {

    TextView textViewId, textViewUsername, textViewEmail, textViewGender;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        super.onCreateDrawer(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        textViewId = (TextView) findViewById(R.id.textViewId);
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewGender = (TextView) findViewById(R.id.textViewGender);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);


        //getting the current user
        User user = SharedPreferencesManager.getInstance(this).getUser();


        String ret = saveFirebaseToken(regId, user.getID());
        JSONObject obj = null;
        try {
            obj = new JSONObject(ret);
            if (obj.getString("error").equals("FALSE")) {
                Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Either there is a problem with server or the user token has already been registered.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //setting the values to the textviews
        textViewId.setText(String.valueOf(user.getID()));
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());


        //if user presses on not registered
        findViewById(R.id.createNotificationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), CreateNotificationActivity.class));

            }
        });
        findViewById(R.id.viewReceivedNotificationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), myReceivedNotificationActivity.class));

            }
        });
        findViewById(R.id.viewsentNotificationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), mySentNotificationActivity.class));

            }
        });

        findViewById(R.id.viewEM5Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), EmergencyFiveListActivity.class));

            }
        });


        findViewById(R.id.homepageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), Homepage.class));

            }
        });

        findViewById(R.id.blogButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), Blog.class));

            }
        });

        findViewById(R.id.eventMapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), EventMap.class));

            }
        });

    }


    private String saveFirebaseToken(String token, int ID) {
        RequestHandler r = new RequestHandler();

        //Toast.makeText(getApplicationContext(), regId, Toast.LENGTH_SHORT).show();

        //creating request parameters
        HashMap<String, String> paramss = new HashMap<>();
        paramss.put("token", token);
        paramss.put("uid", "" + ID);

        //returing the response
        String s = r.sendGetRequest(Constants.URL_addFireBaseToken + "&", paramss);

        return s;
    }

    private String sendGetRequest(String requestURL, HashMap<String, String> postDataParams) {
        RequestHandler r = new RequestHandler();
        URL url;

        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));


            writer.write(r.getPostDataString(postDataParams));

            Log.e("RequestHandlerPost Data", r.getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;

                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


}



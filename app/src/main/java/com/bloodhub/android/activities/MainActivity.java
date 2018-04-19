package com.bloodhub.android.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by izelgurbuz on 3.02.2018.
 */

public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the user is already logged in we will directly start the profile activity
        if (SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().hide();
        }
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });
        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

    }



}
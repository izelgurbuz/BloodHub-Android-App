package com.bloodhub.android;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bloodhub.android.activities.LoginActivity;
import com.bloodhub.android.model.User;

/**
 * Created by izelgurbuz on 3.02.2018.
 */

public class SharedPreferencesManager {

    //the constants
    private static final String SHARED_PREFERENCES_NAME = "bloodHubSharedPreferences";
    private static final String KEY_USERNAME = "";
    private static final String KEY_EMAIL = "";
    //private static final int KEY_ID = 0;
    private static final String KEY_FIRSTNAME = "";
    private static final String KEY_SURNAME = "";
    private static final String KEY_BLOODTYPE = "";
    private static final String KEY_BIRTHDATE = "";
    private static final String KEY_ADDRESS = "";

    private static final int KEY_ID = -1;

    private static SharedPreferencesManager spManagerInstance;
    private static Context mCtx;

    private SharedPreferencesManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (spManagerInstance == null) {
            spManagerInstance = new SharedPreferencesManager(context);
        }
        return spManagerInstance;
    }

    // to the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {



        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("KEY_ID", user.getID());
        editor.putString("KEY_USERNAME", user.getUsername());
        editor.putString("KEY_EMAIL", user.getEmail());
        editor.putString("KEY_SURNAME", user.getSurname());
        editor.putString("KEY_FIRSTNAME", user.getFirstname());
        editor.putString("KEY_BLOODTYPE", user.getBloodType());
        //editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putString("KEY_BIRTHDATE", user.getBirthdate());

        editor.apply();
    }



    //whether user has already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("KEY_USERNAME", null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);


        return new User(

                sharedPreferences.getInt("KEY_ID", -1),
                sharedPreferences.getString("KEY_USERNAME", null),
                sharedPreferences.getString("KEY_EMAIL", null),
                sharedPreferences.getString("KEY_SURNAME", null),
                sharedPreferences.getString("KEY_FIRSTNAME", null),
                sharedPreferences.getString("KEY_BLOODTYPE", null),
                sharedPreferences.getString("KEY_BIRTHDATE", null),
                sharedPreferences.getString("KEY_ADDRESS", null),
                sharedPreferences.getString("KEY_TELEPHONE", null)

                );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}

package com.bloodhub.android.activities;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

public class LoginActivity extends AppCompatActivity{

    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the user is already logged in we will directly start the profile activity
        if (SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        setContentView(R.layout.activity_login);
        if(getActionBar()!= null) {
            getActionBar().hide();
        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();


            }
        });

        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });
    }


    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {

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
                        JSONObject userInstance = obj.getJSONObject("user");


                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response


                        //creating a new user object
                        User user = new User(
                                userInstance.getInt("id"),
                                userInstance.getString("username"),
                                userInstance.getString("email"),
                                userInstance.getString("surname"),
                                userInstance.getString("firstname"),
                                userInstance.getString("bloodType"),
                                userInstance.getString("birthdate"),
                                userInstance.getString("address"),
                                userInstance.getString("telephone")


                                );
                        //Log.e("USERNAME:  ", ""+user.getUsername());
                        //storing the user in shared preferences
                        SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(user);



                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    } else {
                        AlertDialog.Builder alert =  new AlertDialog.Builder(LoginActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Error" );
                        alert.setMessage( obj.getString("error_msg") );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                SharedPreferencesManager.getInstance(getApplicationContext()).logout();
                            }
                        });
                        alert.setCancelable(false);
                        alert.setIcon(R.drawable.cancel);

                            /*alert.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d( "AlertDialog", "Negative" );
                                }
                            } );*/
                        AlertDialog alertDialog = alert.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        //Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
                params.put("email", username);
                params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_Login, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();


    }
}

package com.bloodhub.android.activities;

import android.app.DatePickerDialog;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import java.text.SimpleDateFormat;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Calendar;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.app.Config;
import com.bloodhub.android.model.User;
import com.bloodhub.android.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;






public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {




    EditText editTextUsername, editTextEmail, editTextPassword, editTextFirstName, editTextSurName,
            editTextIdentity, editTextAddress, editTextTelephone,editTextBirthdate;
    Spinner spinner;

    String bloodType = "";
    String real_bloodType = "";

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    Calendar myCalendar = Calendar.getInstance();
    EditText edittext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }


        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextFirstName =  (EditText) findViewById(R.id.editTextFirstName);
        editTextSurName = (EditText) findViewById(R.id.editTextSurName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextIdentity = (EditText) findViewById(R.id.editTextIdentityNumber);
        spinner = (Spinner) findViewById(R.id.spinner1);
        editTextTelephone = (EditText) findViewById(R.id.editTextTelephone);
        editTextBirthdate = (EditText) findViewById(R.id.editTextBirthdate);


        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        txtMessage = (TextView) findViewById(R.id.txt_push_message);

        edittext= (EditText) findViewById(R.id.Birthday);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }



        };

        edittext.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();


        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server

                registerUser();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on login
                //we will open the login screen
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        spinner.setOnItemSelectedListener(this);

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        bloodType = (String)parent.getItemAtPosition(pos);
        switch (bloodType){
            case "ABRh+":
                real_bloodType = "AB%2B";
                break;
            case "ABRh-":
                real_bloodType = "AB-";
                break;
            case "ARh+":
                real_bloodType = "A%2B";
                break;
            case "ARh-":
                real_bloodType = "A-";
                break;
            case "BRh+":
                real_bloodType = "B%2B";
                break;
            case "BRh-":
                real_bloodType = "B-";
                break;
            case "0Rh+":
                real_bloodType = "0%2B";
                break;
            case "0Rh-":
                real_bloodType = "0-";
                break;

        }
        Log.e("kan grubu", real_bloodType);
        //Log.e(TAG, "Selected Item: : " + parent.getItemAtPosition(pos));

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //public boolean isEmpty(String str) {
    // return str.length() == 0;

    //}

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            txtRegId.setText("Firebase Reg Id: " + regId);
        else
            txtRegId.setText("Firebase Reg Id is not received yet!");
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void registerUser() {



        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String firstname = editTextFirstName.getText().toString().trim();
        final String surname = editTextSurName.getText().toString().trim();
        final String identity = editTextIdentity.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();
        final String telephone = editTextTelephone.getText().toString().trim();
        final String birthdate = editTextBirthdate.getText().toString().trim();


    /*if (isEmpty(username)) {
        editTextUsername.setError("Please enter username");
        editTextUsername.requestFocus();
        return;
    }

        if (isEmpty(email)) {
        editTextEmail.setError("Please enter your email");
        editTextEmail.requestFocus();
        return;
    }

       // if (!email.matches(android.util.Patterns.EMAIL_ADDRESS)) {
        //editTextEmail.setError("Enter a valid email");
        //editTextEmail.requestFocus();
        //return;
   // }

        if (isEmpty(password)) {
        editTextPassword.setError("Enter a password");
        editTextPassword.requestFocus();
        return;
    }*/
        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                Log.e("key","girdi");

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("firstname", firstname);
                params.put("surname", surname);
                params.put("identityNum", identity);
                params.put("bloodType", real_bloodType);
                params.put("address", address);
                params.put("telephone", telephone);
                params.put("birthdate", birthdate);

                //returning the response
                return requestHandler.sendPostRequest(Constants.URL_Register, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object

                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("surname"),
                                userJson.getString("firstname"),
                                userJson.getString("bloodType"),
                                userJson.getString("birthdate"),
                                userJson.getString("address"),
                                userJson.getString("telephone")


                                );
                        //storing the user in shared preferences
                        SharedPreferencesManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }




}




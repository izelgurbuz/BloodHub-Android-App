package com.bloodhub.android.activities;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.app.Config;
import com.bloodhub.android.model.Location;
import com.bloodhub.android.model.User;
import com.bloodhub.android.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    EditText editTextUsername, editTextEmail, editTextPassword, editTextFirstName, editTextSurName,
            editTextIdentity, editTextAddress, editTextTelephone, editTextBirthdate;
    Spinner spinner;
    Spinner citySpinner;

    String bloodType = "";
    String real_bloodType = "";

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    Calendar myCalendar = Calendar.getInstance();
    EditText edittext;


    ArrayAdapter<CharSequence> cityAdapter;

    String selectedLocationName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        citySpinner = (Spinner) findViewById(R.id.city_spinner);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        selectedLocationName = "";


        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextSurName = (EditText) findViewById(R.id.editTextSurName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextIdentity = (EditText) findViewById(R.id.editTextIdentityNumber);
        spinner = (Spinner) findViewById(R.id.spinner1);
        editTextTelephone = (EditText) findViewById(R.id.editTextTelephone);
        editTextBirthdate = (EditText) findViewById(R.id.editTextBirthdate);


        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        //txtRegId = (TextView) findViewById(R.id.txt_reg_id);
        //txtMessage = (TextView) findViewById(R.id.txt_push_message);



        edittext = (EditText) findViewById(R.id.editTextBirthdate);


        cityAdapter = ArrayAdapter.createFromResource(this, R.array.sehirler_arr, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);


        citySpinner.setFocusable(true);



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
                if (selectedLocationName == "" || selectedLocationName.equalsIgnoreCase("Please Select One")){

                    findViewById(R.id.city_spinner).requestFocus();
                    findViewById(R.id.city_spinner).performClick();
                }

                else if (TextUtils.isEmpty(editTextUsername.getText().toString().trim())) {
                    editTextUsername.setError("Please enter your Username");
                    editTextUsername.requestFocus();

                }

                else if (TextUtils.isEmpty(editTextFirstName.getText().toString().trim())) {
                    editTextFirstName.setError("Please enter your First Name");
                    editTextFirstName.requestFocus();

                }

                else if (TextUtils.isEmpty(editTextSurName.getText().toString().trim())) {
                    editTextSurName.setError("Please enter your Last Name");
                    editTextSurName.requestFocus();

                }

                else if (TextUtils.isEmpty(editTextIdentity.getText().toString().trim()) || editTextIdentity.getText().toString().trim().length() != 11) {
                    editTextIdentity.setError("Please enter your Identity Number");
                    editTextIdentity.requestFocus();

                }

                else if (TextUtils.isEmpty(editTextAddress.getText().toString().trim())) {
                    editTextAddress.setError("Please enter your Address");
                    editTextAddress.requestFocus();

                }

                else if (TextUtils.isEmpty(editTextBirthdate.getText().toString().trim()) ) {
                    editTextBirthdate.setError("Please select your Birthdate");
                    editTextBirthdate.requestFocus();

                }

                else if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())) {
                    editTextEmail.setError("Please enter your Email");
                    editTextEmail.requestFocus();

                }

                else if (TextUtils.isEmpty(editTextPassword.getText().toString().trim())) {
                    editTextPassword.setError("Please enter a password");
                    editTextPassword.requestFocus();

                }

                else{
                    WebView wv = new WebView(RegisterActivity.this);
                    wv.loadUrl("http://cs491-2.mustafaculban.net/misc/tac.html");
                    wv.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);

                            return true;
                        }
                    });
                    AlertDialog.Builder alert =  new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme );
                    alert.setTitle( "Terms and Conditions" );
                    alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                registerUser();
                        }
                    });
                    alert.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                    } );

                    alert.setView(wv);
                    alert.setCancelable(false);
                    alert.setIcon(R.drawable.check_icon);


                    AlertDialog alertDialog = alert.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();



                }
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloodType_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        citySpinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSurName.getWindowToken(), 0);
                return false;
            }
        }) ;

        findViewById(R.id.spinner1).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSurName.getWindowToken(), 0);
                return false;
            }
        }) ;

        findViewById(R.id.register_scroolview).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSurName.getWindowToken(), 0);

                return false;
            }
        });


        citySpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                selectedLocationName = (String) parent.getItemAtPosition(pos);

                Log.e("sehir",selectedLocationName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                selectedLocationName = (String) arg0.getItemAtPosition(0);
            }
        });

        Log.e("sehir",selectedLocationName);

    }

    private void updateLabel() {
        String myFormat = "ddMMyyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        bloodType = (String) parent.getItemAtPosition(pos);
        switch (bloodType) {
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

        //if (!TextUtils.isEmpty(regId))
          //  txtRegId.setText("Firebase Reg Id: " + regId);
        //else
            //txtRegId.setText("Firebase Reg Id is not received yet!");
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



        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                Log.e("key", "girdi");

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
                params.put("city", selectedLocationName);
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
                    Log.e("gelen",s);
                    //converting response to json object

                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (obj.getString("error").equals("FALSE")) {
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

                        AlertDialog.Builder alert =  new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Success" );
                        alert.setMessage( "You succesfully registered. Please go to your Email and confirm your account." );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
                        alert.setCancelable(false);
                        alert.setIcon(R.drawable.check_icon);

                        /*alert.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Negative" );
                            }
                        } );*/
                        AlertDialog alertDialog = alert.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    } else {
                        AlertDialog.Builder alert =  new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Error" );
                        alert.setMessage( obj.getString("error_msg") );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

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
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }




}




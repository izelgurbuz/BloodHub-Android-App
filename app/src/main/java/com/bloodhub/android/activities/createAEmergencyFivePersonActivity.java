package com.bloodhub.android.activities;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by izelgurbuz on 28.02.2018.
 */

public class createAEmergencyFivePersonActivity extends BaseActivity {

    EditText title, msg;
    int patientID;
    int bloodRequestID;
    User user;
    String classname ;
    double latitude, longitude;
    String placeName;
    EditText email;
    Button buttonSendReq;

    createAEmergencyFivePersonActivity thisclass = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaemergencyfiveperson);
        super.onCreateDrawer(savedInstanceState);
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        buttonSendReq = (Button)findViewById(R.id.buttonSendReq);
        email = (EditText)findViewById(R.id.EM5email);
        user = SharedPreferencesManager.getInstance(this).getUser();


        buttonSendReq.setBackgroundResource(R.drawable.new_em5person_btn);
        LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, 1.0f);
        btnparams.gravity = Gravity.CENTER;
        buttonSendReq.setLayoutParams(btnparams);
        buttonSendReq.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams etxtparams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, 1.0f);
        etxtparams.gravity = Gravity.CENTER;
        email.setLayoutParams(etxtparams);
        email.setBackgroundResource(R.drawable.rect_text_edit);

        buttonSendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmailValid(email.getText().toString())){
                    AlertDialog.Builder alert =  new AlertDialog.Builder(createAEmergencyFivePersonActivity.this, R.style.MyDialogTheme );
                    alert.setTitle( "Confirm" );
                    alert.setMessage( "You are sending EM5 request to "+email.getText().toString()+". Do you confirm ?");
                    alert.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendNotificationResponse();
                        }
                    });
                    alert.setCancelable(false);
                    alert.setIcon(R.drawable.question_mark);

                    alert.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    } );
                    AlertDialog alertDialog = alert.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();

                }
                else{
                    AlertDialog.Builder alert =  new AlertDialog.Builder(createAEmergencyFivePersonActivity.this, R.style.MyDialogTheme );
                    alert.setTitle( "Error" );
                    alert.setMessage( "You entered wrong email. Please correct and try again" );
                    alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setCancelable(false);
                    alert.setIcon(R.drawable.cancel);

                    AlertDialog alertDialog = alert.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }
            }
        });

    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    private void sendNotificationResponse() {

        final int uid = user.getID();
        final String requestedEmail = email.getText().toString();
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


                        AlertDialog.Builder alert =  new AlertDialog.Builder(createAEmergencyFivePersonActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Success" );
                        alert.setMessage( obj.getString("success") );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), EmergencyFiveListActivity.class));
                            }
                        });
                        alert.setCancelable(false);
                        alert.setIcon(R.drawable.check_icon);

                        AlertDialog alertDialog = alert.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();


                    } else {
                        AlertDialog.Builder alert =  new AlertDialog.Builder(createAEmergencyFivePersonActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Error" );
                        alert.setMessage( obj.getString("error_msg") );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), EmergencyFiveListActivity.class));
                            }
                        });
                        alert.setCancelable(false);
                        alert.setIcon(R.drawable.cancel);

                        AlertDialog alertDialog = alert.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
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

                params.put("uid", uid+"");
                params.put("email", requestedEmail);


                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_addToUsersEM5List, params);

            }
        }

        notificationResponse ul = new notificationResponse();
        ul.execute();


    }
}
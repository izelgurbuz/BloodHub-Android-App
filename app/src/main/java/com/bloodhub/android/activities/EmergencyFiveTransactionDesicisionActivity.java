package com.bloodhub.android.activities;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import static android.R.attr.name;


/**
 * Created by izelgurbuz on 28.02.2018.
 */

public class EmergencyFiveTransactionDesicisionActivity extends AppCompatActivity {

    TextView nameEmailText, statusText, changePrompt ,emailText;
    Button rjbtn, cnfbtn;
    String status ;
    String fullName;
    int userID;
    int ownerID;
    String classname ;
    String email;
    LinearLayout upperFrame, middle;

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



        upperFrame = (LinearLayout)findViewById(R.id.upperFrame) ;
        middle = (LinearLayout)findViewById(R.id.middle) ;

        nameEmailText = (TextView) findViewById(R.id.transactionXMLname);
        emailText = (TextView) findViewById(R.id.transactionXMLemail);
        changePrompt = (TextView) findViewById(R.id.transactionXMLprompt);
        statusText = (TextView) findViewById(R.id.transactionXMLstatus);
        rjbtn = (Button)(findViewById(R.id.transactionXMLbuttonConfirm));
        cnfbtn= (Button)(findViewById(R.id.transactionXMLbuttonReject));

        LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, 1.0f);
        btnparams.gravity = Gravity.CENTER;
        middle.setLayoutParams(btnparams);

        upperFrame.setBackgroundResource(R.drawable.my_fullframe_layout);


        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
        );

        LinearLayout.LayoutParams statusparam = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
        );

        LinearLayout.LayoutParams promptparam = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT
        );
        param.setMargins(0,15,0,0);
        statusparam.setMargins(5,50,5,0);
        promptparam.setMargins(10,100,10,10);
        nameEmailText.setLayoutParams(param);
        emailText.setLayoutParams(param);
        changePrompt.setLayoutParams(promptparam);
        statusText.setLayoutParams(statusparam);

        nameEmailText.setText("Status of "+ fullName +" :" );
        nameEmailText.setTextColor(Color.DKGRAY);
        emailText.setText("( "+ email +" ) ");
        emailText.setTextColor(Color.DKGRAY);
        changePrompt.setText("If you want to change your decision please press the button below :");
        changePrompt.setTextColor(Color.DKGRAY);
        statusText.setText(status);






        if(status.equals("Confirmed")){

            cnfbtn.setVisibility(View.GONE);
            rjbtn.setVisibility(View.VISIBLE);
            rjbtn.setText("REJECT");
            rjbtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            rjbtn.setTextColor(Color.WHITE);
            rjbtn.setBackgroundColor(Color.RED);
            statusText.setTextColor(Color.GREEN);
            cnfbtn.setBackgroundResource(R.drawable.my_confirm_button);
            rjbtn.setBackgroundResource(R.drawable.my_reject_button);

        }
        else if(status.equals("Rejected")){
            statusText.setTextColor(Color.RED);

            rjbtn.setVisibility(View.GONE);
            cnfbtn.setVisibility(View.VISIBLE);
            cnfbtn.setText("CONFIRM");
            cnfbtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            cnfbtn.setTextColor(Color.WHITE);
            cnfbtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.confgreen, null));
            cnfbtn.setBackgroundResource(R.drawable.my_confirm_button);
            rjbtn.setBackgroundResource(R.drawable.my_reject_button);
            statusText.setTextColor(Color.RED);


        }
        else{

            cnfbtn.setVisibility(View.VISIBLE);
            rjbtn.setVisibility(View.VISIBLE);
            rjbtn.setText("REJECT");
            rjbtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            rjbtn.setTextColor(Color.WHITE);
            rjbtn.setBackgroundColor(Color.RED);
            cnfbtn.setText("CONFIRM");
            cnfbtn.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            cnfbtn.setTextColor(Color.WHITE);
            cnfbtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.confgreen, null));
            cnfbtn.setBackgroundResource(R.drawable.my_confirm_button);
            rjbtn.setBackgroundResource(R.drawable.my_reject_button);
            statusText.setTextColor(Color.BLUE);

        }

        rjbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert =  new AlertDialog.Builder(EmergencyFiveTransactionDesicisionActivity.this, R.style.MyDialogTheme );
                alert.setTitle( "Rejecting" );
                alert.setMessage( "Do you confirm?" );
                alert.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendTransactionChoice("-1");

                    }
                });
                alert.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alert.setCancelable(false);
                alert.setIcon(R.drawable.question_mark);

                AlertDialog alertDialog = alert.show();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();



            }
        });
        cnfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert =  new AlertDialog.Builder(EmergencyFiveTransactionDesicisionActivity.this, R.style.MyDialogTheme );
                alert.setTitle( "Confirming" );
                alert.setMessage( "Do you confirm?" );
                alert.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendTransactionChoice("1");

                    }
                });
                alert.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alert.setCancelable(false);
                alert.setIcon(R.drawable.question_mark);

                AlertDialog alertDialog = alert.show();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();



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

                        AlertDialog.Builder alert =  new AlertDialog.Builder(EmergencyFiveTransactionDesicisionActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Success" );
                        alert.setMessage( obj.getString("success") );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), EmergencyFiveTransactionActivity.class));

                            }
                        });

                        alert.setCancelable(false);
                        alert.setIcon(R.drawable.check_icon);

                        AlertDialog alertDialog = alert.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();



                    } else {
                        AlertDialog.Builder alert =  new AlertDialog.Builder(EmergencyFiveTransactionDesicisionActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Error" );
                        alert.setMessage( obj.getString("error_msg") );
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), EmergencyFiveTransactionActivity.class));
        finish();
    }
}

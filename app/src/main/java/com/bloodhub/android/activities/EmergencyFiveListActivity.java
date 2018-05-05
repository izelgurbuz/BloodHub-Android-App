package com.bloodhub.android.activities;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.model.EmergencyFive;
import com.bloodhub.android.model.Notification;
import com.bloodhub.android.model.User;
import com.bloodhub.android.views.FabView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.R.attr.button;
import static android.R.attr.name;
import static android.util.LayoutDirection.LTR;
import static android.util.LayoutDirection.RTL;
import static com.bloodhub.android.R.id.add;
import static com.bloodhub.android.R.id.editTextPassword;
import static com.bloodhub.android.R.id.editTextUsername;
import static com.bloodhub.android.R.id.middle;

/**
 * Created by mustafaculban on 3.03.2018.
 */

public class EmergencyFiveListActivity extends BaseActivity {

    int uid;
    EmergencyFiveListActivity thisclass = this;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    LinkedHashMap<Integer, EmergencyFive> aEM5list = new LinkedHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyfivelist);
        super.onCreateDrawer(savedInstanceState);
        lm = (LinearLayout) findViewById(R.id.linearMain);
        // create the layout params that will be used to define how your
        // button will be displayed

        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT,1.0f);

        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }



        uid = SharedPreferencesManager.getInstance(this).getUser().getID();

        displayEM5();

    }

    private void displayEM5() {

        class displayEM5 extends AsyncTask<Void, Void, String> {

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
                        JSONObject em5list = obj.getJSONObject("em5List"); //,"UTF-8" ;
                        JSONArray jsonarray = new JSONArray((em5list.getString("people")));



                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            aEM5list.put(jsonobject.getInt("id"),
                                    new EmergencyFive(
                                            jsonobject.getInt("id"),
                                            jsonobject.getString("fullname"),
                                            jsonobject.getString("email"),
                                            jsonobject.getInt("status"),
                                            jsonobject.getString("dateOfRequest"),
                                            jsonobject.getString("transactionDate"),
                                            jsonobject.getString("telephone")

                                    ));


                        }

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;
                        int count=0;

                        for (Map.Entry<Integer, EmergencyFive> entry : aEM5list.entrySet()) {


                            int id = entry.getKey();
                            final EmergencyFive em5Object = entry.getValue();

                            LinearLayout ll = new LinearLayout(thisclass);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            ll.setBackgroundResource(R.drawable.my_transaction_layout);
                            LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(
                                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, 1.0f);
                            llparam.setMargins(0,10,0,10);
                            ll.setLayoutParams(llparam);


                            // Create TextView
                            TextView first_name = new TextView(thisclass);
                            TextView first_status = new TextView(thisclass);
                            ImageButton phone_call = new ImageButton(thisclass);

                            first_name.setGravity(Gravity.CENTER_VERTICAL );
                            first_status.setGravity(Gravity.CENTER_VERTICAL );

                            first_name.setPadding(20,10,10,10);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    ActionBar.LayoutParams.WRAP_CONTENT,
                                    ActionBar.LayoutParams.MATCH_PARENT,
                                    2.0f
                            );
                            first_name.setWidth(width/4);
                            first_name.setLayoutParams(param);


                            first_name.setTextColor(Color.DKGRAY);


                            first_status.setPadding(20,10,10,10);
                            first_status.setWidth(width/4);
                            first_status.setLayoutParams(param);


                            phone_call.setLayoutParams(params);
                            phone_call.setBackgroundResource(R.drawable.my_phone_button);

                            phone_call.setImageResource(R.drawable.phone);
                            //phone_call.setBackgroundResource(R.drawable.my_transaction_button);
                            phone_call.setVisibility(View.INVISIBLE);


                            String status = em5Object.getStatus() == 1 ? "Confirmed" : (em5Object.getStatus() == -1 ? "Rejected" : "Waiting");
                            first_status.setText(status);
                            first_status.setTypeface(null, Typeface.BOLD);

                            //for phone number to be visible
                            if (em5Object.getStatus() == 1) {
                                first_status.setTextColor(Color.GREEN);
                                count++;
                                phone_call.setVisibility(View.VISIBLE);
                                phone_call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.e("telbutton", "button basildi");
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + em5Object.getTelephone()));

                                        if (ActivityCompat.checkSelfPermission(thisclass, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        }
                                        startActivity(intent);

                                    }

                                });
                                //   } catch (JSONException e) {
                                //  e.printStackTrace();
                            }
                            else if(em5Object.getStatus() == -1)
                                first_status.setTextColor(Color.RED);
                            else{
                                first_status.setTextColor(Color.BLUE);
                                count++;
                            }


                            first_name.setText(em5Object.getFullname()  );


                            ll.addView(first_name);
                            ll.addView(first_status);
                            //ll.addView(sendButton);
                            ll.addView(phone_call);


                            lm.addView(ll);


                        }

                        if(count <5){
                            Button addNewButton = new Button(thisclass);
                            addNewButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                            //addNewButton.setBackgroundResource(R.drawable.send);
                            //addNewButton.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                            addNewButton.setText("+");
                            addNewButton.setTextColor(Color.WHITE);


                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    150,150);

                            layoutParams.setLayoutDirection(RTL);
                            addNewButton.setBackgroundResource(R.drawable.my_addnew_button);

                            addNewButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getApplicationContext(), createAEmergencyFivePersonActivity.class));
                                    Log.e("telbuton", "addNewButton tiklandi");
                                    //method("first", uid);
                                }

                            });
                            layoutParams.setMargins(0,20,0,0);
                            addNewButton.setLayoutParams(layoutParams);
                            addNewButton.setBackgroundResource(R.drawable.my_addnew_button);

                            LinearLayout addBtnLyt = new LinearLayout(thisclass);
                            addBtnLyt.setOrientation(LinearLayout.HORIZONTAL);
                            addBtnLyt.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                            addBtnLyt.addView(addNewButton);
                            lm.addView(addBtnLyt);

                        }

                    } else {

                        AlertDialog.Builder alert =  new AlertDialog.Builder(EmergencyFiveListActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Info" );
                        alert.setMessage( "You don't have any person in your EM5 list. Please add someone." );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), createAEmergencyFivePersonActivity.class));

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


                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_getEM5List, params);
            }
        }

        displayEM5 ul = new displayEM5();
        ul.execute();


    }

    private void approveOrReject(String email, int uid) {


        //if everything is fine

        class SendToServerTask extends AsyncTask<Void, Void, String> {

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

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                //params.put("email", username);
                //params.put("password", password);

                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_Login, params);
            }
        }

        SendToServerTask ul = new SendToServerTask();
        ul.execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.emerg5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
    }
}






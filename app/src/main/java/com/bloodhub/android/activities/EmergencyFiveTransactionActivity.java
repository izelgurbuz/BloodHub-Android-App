package com.bloodhub.android.activities;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.bloodhub.android.model.EmergencyFive;
import com.bloodhub.android.model.Notification;
import com.bloodhub.android.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.R.attr.data;
import static com.bloodhub.android.R.id.editTextPassword;
import static com.bloodhub.android.R.id.editTextUsername;
import static com.bloodhub.android.R.id.msg;

/**
 * Created by izelgurbuz on 28.02.2018.
 */

public class EmergencyFiveTransactionActivity extends BaseActivity {

    int uid;
    EmergencyFiveTransactionActivity thisclass = this;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    LinkedHashMap<Integer, EmergencyFive> aEM5list = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyfivetransaction);
        super.onCreateDrawer(savedInstanceState);
        lm = (LinearLayout) findViewById(R.id.em5TransactionlinearMain);
        // create the layout params that will be used to define how your
        // button will be displayed

        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lm.setLayoutParams(params);
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        uid = SharedPreferencesManager.getInstance(this).getUser().getID();

        displayWaitingTransactions();



    }

    private void displayWaitingTransactions() {



        class WaitingTransactions extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar = (ProgressBar) findViewById(R.id.em5TransactionprogressBar);
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

                        JSONObject JSONem5list = obj.getJSONObject("em5List"); //,"UTF-8" ;

                        JSONArray jsonarray = new JSONArray((JSONem5list.getString("people")));

                        int j=0;

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);

                            LinearLayout ll = new LinearLayout(thisclass);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );

                            param.setMargins(0, 12, 0, 12);
                            ll.setLayoutParams(param);

                            TextView name = new TextView(thisclass);
                            name.setText(jsonobject.getString("fullname"));
                            param = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    1.0f
                            );
                            name.setLayoutParams(param);
                            name.setTextSize(16);
                            name.setGravity(Gravity.CENTER_VERTICAL);
                            ll.addView(name);

                            String statustext = jsonobject.getInt("status") == 1 ? "Confirmed" : (jsonobject.getInt("status") == -1 ? "Rejected" : "Waiting");
                            TextView status = new TextView(thisclass);
                            status.setText(statustext);
                            final String statusstr= statustext;
                            param = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    2.0f
                            );
                            status.setLayoutParams(param);
                            status.setTextSize(12);
                            status.setGravity(Gravity.CENTER_VERTICAL);
                            ll.addView(status);

                            final String fullName= jsonobject.getString("fullname");
                            final int userID = jsonobject.getInt("id");
                            final String email = jsonobject.getString("email");
                            final int ownerID = JSONem5list.getInt("ownerID");


                            // Create Button
                            final ImageButton btn = new ImageButton(thisclass);
                            // Give button an ID
                            btn.setId(j + 1);
                            btn.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward));
                            btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                            // set the layoutParams on the button
                            param = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    2.0f
                            );
                            param.setMargins(0, 4, 0, 4);


                            btn.setLayoutParams(param);

                            final int index = j;
                            // Set click listener for button
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    Log.i("TAG", "index :" + index);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("statusstr", statusstr);
                                    bundle.putString("fullName", fullName);
                                    bundle.putString("email", email);
                                    bundle.putInt("userID",userID);
                                    bundle.putInt("ownerID",ownerID);

                                    Intent i = new Intent(thisclass, EmergencyFiveTransactionDesicisionActivity.class);
                                    i.putExtras(bundle);
                                    //i.putExtra("titlestr", titlestr);
                                    startActivity(i);
//                                    finish();

                                    //Add button to LinearLayout defined in XML
                                    Toast.makeText(getApplicationContext(),
                                            "Clicked Button Index :" + index,
                                            Toast.LENGTH_LONG).show();
                                    //startActivity(new Intent(getApplicationContext(), notificationResponseActivity.class));


                                }
                            });

                            ll.addView(btn);

                            lm.addView(ll);




                        }

                        j =0;

                        /*
                        for (Map.Entry<Integer, EmergencyFive> entry : aEM5list.entrySet()) {
                            int id = entry.getKey();
                            EmergencyFive em5list = entry.getValue();


                            LinearLayout ll = new LinearLayout(thisclass);
                            ll.setOrientation(LinearLayout.HORIZONTAL);

                            // Create TextView
                            TextView name = new TextView(thisclass);
                            name.setText(em5list.getFullname());
                            //Log.e("notifi"+id, notification.getDatestamp());

                            ll.addView(name);

                            // Create TextView
                            String statustext = em5list.getStatus() == 1 ? "Confirmed" : (em5list.getStatus() == -1 ? "Rejected" : "Waiting");
                            TextView status = new TextView(thisclass);
                            status.setText(statustext);
                            final String statusstr= statustext;
                            ll.addView(status);

                            final String fullName= em5list.getFullname();
                            final int userID = em5list.getId();
                            final String email = em5list.getEmail();
                            final int ownerID = JSONem5list.getInt("ownerID");




                            // Create Button
                            final Button btn = new Button(thisclass);
                            // Give button an ID
                            btn.setId(j + 1);
                            btn.setText("->");
                            // set the layoutParams on the button
                            btn.setLayoutParams(params);

                            final int index = j;
                            // Set click listener for button
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    Log.i("TAG", "index :" + index);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("statusstr", statusstr);
                                    bundle.putString("fullName", fullName);
                                    bundle.putString("email", email);
                                    bundle.putInt("userID",userID);
                                    bundle.putInt("ownerID",ownerID);

                                    Intent i = new Intent(thisclass, EmergencyFiveTransactionDesicisionActivity.class);
                                    i.putExtras(bundle);
                                    //i.putExtra("titlestr", titlestr);
                                    startActivity(i);
                                    finish();





                                    //Add button to LinearLayout defined in XML
                                    Toast.makeText(getApplicationContext(),
                                            "Clicked Button Index :" + index,
                                            Toast.LENGTH_LONG).show();
                                    //startActivity(new Intent(getApplicationContext(), notificationResponseActivity.class));


                                }
                            });

                            ll.addView(btn);
                            //Add button to LinearLayout defined in XML
                            lm.addView(ll);
                        }*/


//                        Toast.makeText(getApplicationContext(), "halelujah", Toast.LENGTH_SHORT).show();

                        Log.e("gulbelea","lala");


                        //finish();
                        //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("error"), Toast.LENGTH_SHORT).show();
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
                return requestHandler.sendPostRequest(Constants.URL_getPersonalWaitingEM5List, params);
            }
        }

        WaitingTransactions ul = new WaitingTransactions();
        ul.execute();


    }
}





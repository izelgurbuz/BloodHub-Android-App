package com.bloodhub.android.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mustafaculban on 3.03.2018.
 */

public class EmergencyFiveListActivity extends AppCompatActivity{

    int uid;
    EmergencyFiveListActivity thisclass = this;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    LinkedHashMap<Integer, Notification> notifications = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyfivelist);
        lm = (LinearLayout) findViewById(R.id.linearMain);
        // create the layout params that will be used to define how your
        // button will be displayed

        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
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
                        JSONObject em5list = obj.getJSONObject("em5list"); //,"UTF-8" ;
                        
                        ((TextView)findViewById(R.id.textViewEM5List)).setText("Your EM5 List is as below");

                        Toast.makeText(getApplicationContext(), em5list.getString("first_name"), Toast.LENGTH_SHORT).show();

                        LinearLayout ll = new LinearLayout(thisclass);
                        ll.setOrientation(LinearLayout.HORIZONTAL);

                        // Create TextView
                        TextView first_name = new TextView(thisclass);
                        first_name.setText(em5list.getString("first_name") + "   " + em5list.getString("first_email") +"   "+
                                (em5list.getInt("isApprovedforFirstPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforFirstPerson") == -1 ? "Rejected" : "Waiting") ));
                        first_name.setPadding(0,10,0,20);
                        ll.addView(first_name);

                        lm.addView(ll);

                        LinearLayout ll2 = new LinearLayout(thisclass);
                        ll2.setOrientation(LinearLayout.HORIZONTAL);
                        TextView second_name = new TextView(thisclass);
                        second_name.setText(em5list.getString("second_name") + "   " + em5list.getString("second_email") + "     "+
                                (em5list.getInt("isApprovedforSecondPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforSecondPerson") == -1 ? "Rejected" : "Waiting") ));
                        second_name.setPadding(0,10,0,20);
                        ll2.addView(second_name);
                        lm.addView(ll2);

                        LinearLayout ll3 = new LinearLayout(thisclass);
                        ll3.setOrientation(LinearLayout.HORIZONTAL);
                        TextView third_name = new TextView(thisclass);
                        third_name.setText(em5list.getString("third_name") + "   " + em5list.getString("third_email")+ "     "+
                                (em5list.getInt("isApprovedforThirdPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforThirdPerson") == -1 ? "Rejected" : "Waiting") ));
                        third_name.setPadding(0,10,0,20);
                        ll3.addView(third_name);
                        lm.addView(ll3);

                        LinearLayout ll4 = new LinearLayout(thisclass);
                        ll4.setOrientation(LinearLayout.HORIZONTAL);
                        TextView fourth_name = new TextView(thisclass);
                        fourth_name.setText(em5list.getString("fourth_name") + "   " + em5list.getString("fourth_email")+ "     "+
                                (em5list.getInt("isApprovedforFourthPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforFourthPerson") == -1 ? "Rejected" : "Waiting") ));
                        fourth_name.setPadding(0,10,0,20);
                        ll4.addView(fourth_name);
                        lm.addView(ll4);

                        LinearLayout ll5 = new LinearLayout(thisclass);
                        ll5.setOrientation(LinearLayout.HORIZONTAL);
                        TextView fifth_name = new TextView(thisclass);
                        fifth_name.setText(em5list.getString("fifth_name") + "   " + em5list.getString("fifth_email")+ "     "+
                                (em5list.getInt("isApprovedforFifthPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforFifthPerson") == -1 ? "Rejected" : "Waiting") ));
                        fifth_name.setPadding(0,10,0,10);
                        ll5.addView(fifth_name);
                        lm.addView(ll5);

                        //starting the profile activity
                        //finish();
                        //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    } else {
                        ((TextView)findViewById(R.id.textViewEM5List)).setText("You dont have EM5 List . \n Lets create one...");
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
                params.put("uid", uid+"");


                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_getEM5List, params);
            }
        }

        displayEM5 ul = new displayEM5();
        ul.execute();


    }
}






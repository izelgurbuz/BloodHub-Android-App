package com.bloodhub.android.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.R.attr.button;
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
    LinkedHashMap<Integer, Notification> notifications = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencyfivelist);
        super.onCreateDrawer(savedInstanceState);
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
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;


                        // Create TextView
                        EditText first_name = new EditText(thisclass);
                        TextView first_status = new TextView(thisclass);
                        //FloatingActionButton phone_call = new FloatingActionButton(thisclass);
                        first_name.setLayoutParams(new AppBarLayout.LayoutParams(width/2,height/8));
                        first_status.setLayoutParams(new AppBarLayout.LayoutParams(width/6, first_name.getLayoutParams().height));
                        Button sendButton = new Button(thisclass);
                        sendButton.setLayoutParams(new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        sendButton.setBackgroundResource(R.drawable.send);
                        sendButton.getLayoutParams().height=height/8;
                        sendButton.getLayoutParams().width=width/6;
                        int difference= height/8 - width/6;
                        sendButton.setOnClickListener(new View.OnClickListener(){
                          @Override
                         public void onClick(View view) {
                                //method("first", uid);
                           }

                         });
                        String status = em5list.getInt("isApprovedforFirstPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforFirstPerson") == -1 ? "Rejected" : "Waiting") ;
                        first_status.setText(status);
                        if(status.equals("Confirmed"))
                        {
                            //phone_call.setImageResource(R.drawable.phone);
                            //phone_call.setOnClickListener(new View.OnClickListener(){
                              //  @Override
                               // public void onClick(View view) {

                             //   }

                           // });
                     //   } catch (JSONException e) {
                  //  e.printStackTrace();
                }
                first_name.setText(em5list.getString("first_name") + "   " + em5list.getString("first_email") );
                        first_name.setPadding(0,0,0,0);
                        first_status.setPadding(0,difference/2,0,difference/2);
                        sendButton.setPadding(0,difference/2,0,difference/2);

                        first_name.setBackgroundResource(R.drawable.rect_text_edit);
                        first_status.setBackgroundResource(R.drawable.rect_text_edit);
                       // phone_call.setBackgroundResource(R.drawable.rect_text_edit);
                        ll.addView(first_name);
                        ll.addView(first_status);
                        ll.addView(sendButton);




                        lm.addView(ll);

                        LinearLayout ll2 = new LinearLayout(thisclass);
                        ll2.setOrientation(LinearLayout.HORIZONTAL);
                        EditText second_name = new EditText(thisclass);
                        second_name.setText(em5list.getString("second_name") + "   " + em5list.getString("second_email") + "     "+
                                (em5list.getInt("isApprovedforSecondPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforSecondPerson") == -1 ? "Rejected" : "Waiting") ));
                        second_name.setPadding(0,10,0,20);
                        second_name.setBackgroundResource(R.drawable.rect_text_edit);

                        ll2.addView(second_name);
                        lm.addView(ll2);

                        LinearLayout ll3 = new LinearLayout(thisclass);
                        ll3.setOrientation(LinearLayout.HORIZONTAL);
                        EditText third_name = new EditText(thisclass);
                        third_name.setText(em5list.getString("third_name") + "   " + em5list.getString("third_email")+ "     "+
                                (em5list.getInt("isApprovedforThirdPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforThirdPerson") == -1 ? "Rejected" : "Waiting") ));
                        third_name.setPadding(0,10,0,20);
                        third_name.setBackgroundResource(R.drawable.rect_text_edit);

                        ll3.addView(third_name);
                        lm.addView(ll3);

                        LinearLayout ll4 = new LinearLayout(thisclass);
                        ll4.setOrientation(LinearLayout.HORIZONTAL);
                        EditText fourth_name = new EditText(thisclass);
                        fourth_name.setText(em5list.getString("fourth_name") + "   " + em5list.getString("fourth_email")+ "     "+
                                (em5list.getInt("isApprovedforFourthPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforFourthPerson") == -1 ? "Rejected" : "Waiting") ));
                        fourth_name.setPadding(0,10,0,20);
                        fourth_name.setBackgroundResource(R.drawable.rect_text_edit);

                        ll4.addView(fourth_name);
                        lm.addView(ll4);

                        LinearLayout ll5 = new LinearLayout(thisclass);
                        ll5.setOrientation(LinearLayout.HORIZONTAL);
                        EditText fifth_name = new EditText(thisclass);
                        fifth_name.setText(em5list.getString("fifth_name") + "   " + em5list.getString("fifth_email")+ "     "+
                                (em5list.getInt("isApprovedforFifthPerson") == 1 ? "Confirmed" : (em5list.getInt("isApprovedforFifthPerson") == -1 ? "Rejected" : "Waiting") ));
                        fifth_name.setPadding(0,10,0,10);
                        fifth_name.setBackgroundResource(R.drawable.rect_text_edit);

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

    private void userLogin(String email, int uid) {


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
}






package com.bloodhub.android.activities;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

import static com.bloodhub.android.R.id.editTextPassword;
import static com.bloodhub.android.R.id.editTextUsername;

/**
 * Created by izelgurbuz on 28.02.2018.
 */

public class myReceivedNotificationActivity extends AppCompatActivity {

    int uid;
    String msgstr;
    String titlestr;
    myReceivedNotificationActivity thisclass = this;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    HashMap<Integer, Notification> notifications = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreceivednotifications);
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

        displayReceivedNotifications();



    }

    public String getMsgstr(){
        return msgstr;
    }
    public String getTitlestr(){
        return titlestr;
    }
    private void displayReceivedNotifications() {



        class ReceivedNotifications extends AsyncTask<Void, Void, String> {

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

                        JSONArray jsonarray = new JSONArray((obj.getString("receivedNotifications")));
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int id = jsonobject.getInt("id");
                            String msg = jsonobject.getString("msg");
                            int sentUserID = jsonobject.getInt("sentUserID");
                            String type = jsonobject.getString("type");
                            String title = jsonobject.getString("title");
                            String datestamp = jsonobject.getString("datestamp");

                            notifications.put(id,new Notification(id, sentUserID,msg, type, title,datestamp));

                        }
                        int j=0;

                        for (Map.Entry<Integer, Notification> entry : notifications.entrySet()) {
                            int id = entry.getKey();
                            final Notification notification = entry.getValue();


                            LinearLayout ll = new LinearLayout(thisclass);
                            ll.setOrientation(LinearLayout.HORIZONTAL);

                            // Create TextView
                            TextView title = new TextView(thisclass);
                            title.setText(notification.getTitle());
                            titlestr= notification.getTitle();
                            ll.addView(title);

                            // Create TextView
                            TextView msg = new TextView(thisclass);
                            msg.setText("");
                            msgstr= notification.getMsg();
                            ll.addView(msg);

                            // Create Button
                            final Button btn = new Button(thisclass);
                            // Give button an ID
                            btn.setId(j + 1);
                            btn.setText("Feel Yes");
                            // set the layoutParams on the button
                            btn.setLayoutParams(params);

                            final int index = j;
                            // Set click listener for button
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {

                                    Log.i("TAG", "index :" + index);
                                    Intent i = new Intent(thisclass, notificationResponseActivity.class);
                                    i.putExtra("msgstr", msgstr);
                                    i.putExtra("titlestr", titlestr);
                                    startActivity(i);




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
                        }



                        Toast.makeText(getApplicationContext(), "halelujah", Toast.LENGTH_SHORT).show();

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
                return requestHandler.sendPostRequest(Constants.URL_getReceivedNotification, params);
            }
        }

        ReceivedNotifications ul = new ReceivedNotifications();
        ul.execute();


    }
}





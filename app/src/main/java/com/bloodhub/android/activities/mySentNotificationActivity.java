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
import android.util.DisplayMetrics;
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
import java.util.LinkedHashMap;
import java.util.Map;

import static android.R.attr.button;
import static android.R.attr.color;
import static android.R.attr.drawable;
import static com.bloodhub.android.R.raw.notifications;

/**
 * Created by izelgurbuz on 2.03.2018.
 */

public class mySentNotificationActivity extends BaseActivity {

    int uid;
    mySentNotificationActivity thisclass = this;
    LinearLayout lm;
    LinearLayout.LayoutParams params;
    LinkedHashMap<Integer, Notification> notifications = new LinkedHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysentnotifications);
        super.onCreateDrawer(savedInstanceState);
        lm = (LinearLayout) findViewById(R.id.linearMain);
        // create the layout params that will be used to define how your
        // button will be displayed
        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, 1.0f);
        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        uid = SharedPreferencesManager.getInstance(this).getUser().getID();

        displaySentNotifications();
    }
    private void displaySentNotifications() {



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

                        JSONArray jsonarray = new JSONArray((obj.getString("sentNotifications")));
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
                            Log.e("idler", entry.getValue().getId()+"");
                        }
                        for (Map.Entry<Integer, Notification> entry : notifications.entrySet()) {
                            int id = entry.getKey();
                            final Notification notification = entry.getValue();


                            LinearLayout ll = new LinearLayout(thisclass);
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            ll.setBackgroundResource(R.drawable.my_transaction_layout);

                            LinearLayout.LayoutParams llparam = new LinearLayout.LayoutParams(
                                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, 1.0f);
                            llparam.setMargins(10,10,10,10);
                            ll.setLayoutParams(llparam);

                            // Create TextView
                            TextView title = new TextView(thisclass);
                            title.setText(notification.getTitle());
                            Log.e("notifi"+id, notification.getDatestamp());
                            title.setPadding(10,10,10,10);
                            /*LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    ActionBar.LayoutParams.WRAP_CONTENT,
                                    ActionBar.LayoutParams.WRAP_CONTENT,
                                    1.0f
                            );*/
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            int height = displayMetrics.heightPixels;
                            int width = displayMetrics.widthPixels;
                            title.setWidth(width*2/3);
                            title.setLayoutParams(params);


                            //title.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Small);
                            title.setTextColor(Color.DKGRAY);

                            final String titlestr= notification.getTitle();
                            final TextView letter = new TextView(thisclass);
                            String letterstr = titlestr.charAt(0)+"";
                            letter.setText(letterstr.toUpperCase());
                            letter.setTextColor(Color.RED);
                            letter.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                            title.setGravity(Gravity.CENTER_VERTICAL );
                            //letter.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            //letter.setBackgroundResource(R.drawable.my_letter_button);
                            //letter.setWidth(1);

                            letter.setLayoutParams(params);



                            ll.addView(letter);
                            ll.addView(title);

                            // Create TextView
                            TextView msg = new TextView(thisclass);
                            msg.setText("");
                            msg.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgray, null));
                            final String msgstr= notification.getMsg();
                            ll.addView(msg);

                            final int patientID = notification.getSentUserID();
                            final int bloodRequestID = notification.getId();

                            // Create Button
                            final ImageButton btn = new ImageButton(thisclass);
                            // Give button an ID
                            btn.setId(j + 1);
                            //btn.setText("->");
                            // set the layoutParams on the button
                            btn.setLayoutParams(params);
                            btn.setImageResource(R.drawable.ic_menu_send);

                            btn.setBackgroundResource(R.drawable.my_sentnoti_button);
                            LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, 1.0f);
                            btnparams.gravity = Gravity.CENTER;
                            btnparams.setMargins(20,20,20,20);
                            btn.setMaxHeight(20);
                            btn.setMaxWidth(20);
                            btn.setMinimumHeight(20);
                            btn.setMinimumWidth(20);


                            btn.setLayoutParams(btnparams);

                            final int index = j;
                            // Set click listener for button
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    Log.i("TAG", "index :" + index);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("msgstr", msgstr);
                                    bundle.putString("titlestr", titlestr);
                                    bundle.putInt("patientID",patientID);
                                    bundle.putInt("bloodRequestID",bloodRequestID);

                                    bundle.putString("class","s");
                                    Intent i = new Intent(thisclass, notificationResponseActivity.class);
                                    i.putExtras(bundle);
                                    //i.putExtra("titlestr", titlestr);
                                    startActivity(i);






                                    //startActivity(new Intent(getApplicationContext(), notificationResponseActivity.class));


                                }
                            });

                            ll.addView(btn);
                            //Add button to LinearLayout defined in XML
                            lm.addView(ll);
                        }



                        //Toast.makeText(getApplicationContext(), "halelujah", Toast.LENGTH_SHORT).show();




                        //finish();
                        //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    } else {
                        AlertDialog.Builder alert =  new AlertDialog.Builder(mySentNotificationActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Error" );
                        alert.setMessage( obj.getString("error") );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

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
                return requestHandler.sendPostRequest(Constants.URL_getSentNotification, params);
            }
        }

        ReceivedNotifications ul = new ReceivedNotifications();
        ul.execute();


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
    }


}
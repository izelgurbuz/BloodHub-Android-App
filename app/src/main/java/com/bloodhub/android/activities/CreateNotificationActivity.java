package com.bloodhub.android.activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.bloodhub.android.model.Location;

/**
 * Created by izelgurbuz on 3.02.2018.
 */

public class CreateNotificationActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    EditText editTextlocation, editTexthospitalName,
    editTextnameSurname;

    private CheckBox checkBoxSMS, checkBoxPush, checkBoxMail;

    Spinner spinner, citySpinner;

    String Spinnerblood_type , realBlood_Type;

    String notification_type;

    String selectedLocation;
    int selectedLocationID;
    String selectedLocationName;

    int senderID;

    ArrayList<String> cityList=new ArrayList<>();
    ArrayList<String> cityTempList=new ArrayList<>();

    ArrayList<Location> locations = new ArrayList<Location>();

    ArrayAdapter<String> cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
        super.onCreateDrawer(savedInstanceState);
        fillCitySpinner();

        if (!SharedPreferencesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        spinner = (Spinner) findViewById(R.id.blood_spinner);

        citySpinner = (Spinner) findViewById(R.id.city_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloodType_arrays, R.layout.spinner_itm);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        cityAdapter = new ArrayAdapter<String>(this,R.layout.spinner_itm,cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        spinner.setOnItemSelectedListener(this);

        selectedLocationName = "";

        citySpinner.setOnItemSelectedListener( new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if(pos !=0){
                    selectedLocation = locations.get(pos-1).getPlaceName();
                    selectedLocationID = locations.get(pos-1).getID();
                    selectedLocationName = locations.get(pos-1).getCity() +" "+  locations.get(pos-1).getPlaceName();
                    Log.e("location value",selectedLocationID+"");
                }


            }



            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        User user = SharedPreferencesManager.getInstance(this).getUser();

        editTextnameSurname = (EditText) findViewById(R.id.editTextnameSurname);

        editTextnameSurname.setText(user.getFirstname() + " " + user.getSurname());

        senderID = user.getID();

        checkBoxSMS = (CheckBox) findViewById(R.id.checkbox_sms);
        checkBoxMail = (CheckBox) findViewById(R.id.checkbox_mail);
        checkBoxPush = (CheckBox) findViewById(R.id.checkbox_push);


        findViewById(R.id.buttonNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkBoxSMS.isChecked() && !checkBoxMail.isChecked() && !checkBoxPush.isChecked()){
                    AlertDialog.Builder alert =  new AlertDialog.Builder(CreateNotificationActivity.this, R.style.MyDialogTheme );
                    alert.setTitle( "Error" );
                    alert.setMessage( "You have to select one of the notification types." );
                    alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkBoxSMS.setError("You have to select one of the notification types.");
                            dialog.dismiss();
                        }
                    });
                    alert.setCancelable(false);
                    alert.setIcon(R.drawable.cancel);

                    AlertDialog alertDialog = alert.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();


                }
                else if (TextUtils.isEmpty(editTextnameSurname.getText())){
                    editTextnameSurname.setError( "Full Name is required!" );
                    editTextnameSurname.setHint("Full Name is required!" );
                    editTextnameSurname.requestFocus();
                }
                //validating inputs
                else if (selectedLocationName == "" || selectedLocationName.equalsIgnoreCase("Select Blood Center or Hospital")) {
                    AlertDialog.Builder alert =  new AlertDialog.Builder(CreateNotificationActivity.this, R.style.MyDialogTheme );
                    alert.setTitle( "Error" );
                    alert.setMessage( "You have to select one of the hospitals or blood centers fron the list.." );
                    alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            citySpinner.requestFocus();
                            citySpinner.performClick();
                            dialog.dismiss();
                        }
                    });
                    alert.setCancelable(false);
                    alert.setIcon(R.drawable.cancel);

                    AlertDialog alertDialog = alert.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();


                }/*

                else if (TextUtils.isEmpty(editTexthospitalName.getText())) {
                    editTexthospitalName.setError("Please enter Hospital Name");
                    editTexthospitalName.requestFocus();
                }*/

                else {
                    AlertDialog.Builder alert =  new AlertDialog.Builder(CreateNotificationActivity.this, R.style.MyDialogTheme );
                    alert.setTitle( "Confirm" );
                    alert.setMessage( "Do you confirm your Notification Request ?");
                    alert.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendBloodRequest();
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

            }
        });

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_sms:
                if (checked) {

                    checkBoxPush.setChecked(false);
                    checkBoxMail.setChecked(false);
                    notification_type = "sms";

                }
                break;

            case R.id.checkbox_push:
                if (checked) {
                    checkBoxSMS.setChecked(false);
                    checkBoxMail.setChecked(false);
                    notification_type = "push";


                }
                    break;
            case R.id.checkbox_mail:
                if (checked){
                    checkBoxPush.setChecked(false);
                    checkBoxSMS.setChecked(false);
                    notification_type = "mail";
                }
                break;

        }
        Log.e("notification_type", notification_type);
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        Spinnerblood_type = (String) parent.getItemAtPosition(pos);
        switch (Spinnerblood_type){
            case "ABRh+":
                realBlood_Type = "AB+";
                break;
            case "ABRh-":
                realBlood_Type = "AB-";
                break;
            case "ARh+":
                realBlood_Type = "A+";
                break;
            case "ARh-":
                realBlood_Type = "A-";
                break;
            case "BRh+":
                realBlood_Type = "B+";
                break;
            case "BRh-":
                realBlood_Type = "B-";
                break;
            case "0Rh+":
                realBlood_Type = "0+";
                break;
            case "0Rh-":
                realBlood_Type = "0-";
                break;

        }


        Log.e("kan grubu", realBlood_Type);
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onBackPressed() {

        CreateNotificationActivity.this.finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

    }

    private void sendBloodRequest() {

        final String nameSurname = editTextnameSurname.getText().toString();

        class BloodRequest extends AsyncTask<Void, Void, String> {

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

                        AlertDialog.Builder alert =  new AlertDialog.Builder(CreateNotificationActivity.this, R.style.MyDialogTheme );
                        alert.setTitle( "Success" );
                        alert.setMessage( obj.getString("success") );
                        alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), mySentNotificationActivity.class));
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
                        if(obj.has("block")){
                            AlertDialog.Builder alert =  new AlertDialog.Builder(CreateNotificationActivity.this, R.style.MyDialogTheme );
                            alert.setTitle( "Error" );
                            alert.setMessage( obj.getString("error_msg") );
                            alert.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    SharedPreferencesManager.getInstance(getApplicationContext()).logout();
                                }
                            });
                            alert.setCancelable(false);
                            alert.setIcon(R.drawable.cancel);

                            AlertDialog alertDialog = alert.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();


                        }
                        else{
                            AlertDialog.Builder alert =  new AlertDialog.Builder(CreateNotificationActivity.this, R.style.MyDialogTheme );
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

                params.put("name_surname", nameSurname);
                params.put("notificationType", notification_type);
                params.put("bloodType", realBlood_Type+"");
                params.put("locationID", selectedLocationID+"");
                params.put("hospitalName", selectedLocationName);
                params.put("senderID", senderID+"");

                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_sendBloodRequest, params);
            }
        }

        BloodRequest ul = new BloodRequest();
        ul.execute();


    }

    private void fillCitySpinner() {


        //if everything is fine

        class BloodRequest extends AsyncTask<Void, Void, String> {

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
                    //Log.e("JSONOBJEsi:  ", obj.toString(4));
                    //if no error in response

                    Log.e("JSONERROR:  ", obj.getString("error"));

                    if (obj.getString("error").equals("FALSE")) {
                        JSONArray jsonarray = new JSONArray((obj.getString("bloodcenters")));

                        for (int i = 0; i < jsonarray.length(); i++) {
                            final JSONObject jsonObject= jsonarray.getJSONObject(i);
                            locations.add(new Location(jsonObject.getInt("id"), jsonObject.getString("city"), jsonObject.getString("name"),jsonObject.getString("latitude"), jsonObject.getString("longitude")));
                            //Log.e("bloodcentercoor", bloodCentersandCoordinates.get("Ankara").toString());
                        }
                        cityTempList.add("Select Blood Center or Hospital");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            cityTempList.add(locations.get(i).getCity() + " - "+locations.get(i).getPlaceName());//Log.e("bloodcentercoor", bloodCentersandCoordinates.get("Ankara").toString());
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cityList.addAll(cityTempList);
                cityAdapter.notifyDataSetChanged();


            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_getBloodLocations, params);
            }
        }

        BloodRequest ul = new BloodRequest();
        ul.execute();


    }
}

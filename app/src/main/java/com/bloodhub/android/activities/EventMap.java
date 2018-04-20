package com.bloodhub.android.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class EventMap extends FragmentActivity
        implements OnMapReadyCallback {

    String[] isimler = new String[10];
    double[] enlemler = new double[10];
    double[] boylamlar = new double[10];
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        showEvent();
        setContentView(R.layout.activity_event_map);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    private void showEvent() {


        //if everything is fine

        class ShowEvent extends AsyncTask<Void, Void, String> {


            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                // s = "{" + s + "}";
                super.onPostExecute(s);


                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    Log.e("JSONOBJEsi:  ", obj.toString(4));
                    //if no error in response

                    Log.e("JSONERROR:  ", obj.getString("error"));

                    if (obj.getString("error").equals("FALSE")) {
                        JSONArray jsonarray = new JSONArray((obj.getString("events")));
                        length = jsonarray.length();

                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response


                        //creating a new user object
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            isimler[i] = jsonobject.getString("name");
                            enlemler[i] = jsonobject.getDouble("latitude");
                            boylamlar[i] = jsonobject.getDouble("longitude");
                        }

                        //Log.e("USERNAME:  ", ""+user.getUsername());
                        //storing the user in shared preferences


                        //starting the profile activity

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


                //returing the response
                return requestHandler.sendPostRequest(Constants.URL_GetEvents, params);
            }
        }
        ShowEvent ul = new ShowEvent();
        ul.execute();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        //String place =  ;
        //double enlem =   ;
        //double boylam =  ;

        for (int i = 0; i < length; i++) {
            LatLng pstn = new LatLng(enlemler[i], boylamlar[i]);
            googleMap.addMarker(new MarkerOptions().position(pstn)
                    .title(isimler[i]));
        }
        //LatLng etimesgut = new LatLng(enlemler[0],boylamlar[0]);
        //LatLng bilkent = new LatLng(enlemler[1],boylamlar[1]);
        LatLng cankaya = new LatLng(39.843009, 32.72908);
        //googleMap.addMarker(new MarkerOptions().position(etimesgut)
        //        .title(isimler[0]));
        //googleMap.addMarker(new MarkerOptions().position(bilkent)
        //       .title(isimler[1]))
        ;
        //googleMap.addMarker(new MarkerOptions().position(cankaya)
        //        .title("Kızılay Merkezi"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cankaya, 11.5f));

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
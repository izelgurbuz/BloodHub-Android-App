package com.bloodhub.android.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bloodhub.android.Constants;
import com.bloodhub.android.R;
import com.bloodhub.android.RequestHandler;
import com.bloodhub.android.SharedPreferencesManager;
import com.bloodhub.android.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserProfileActivity extends BaseActivity {

    private ProgressBar spinner;
    private EditText oldPassword, newPassword, newPasswordConfirmation;
    private ToggleButton availabilityToggle;
    private Button changePasswordButton;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        super.onCreateDrawer(savedInstanceState);
        uid = SharedPreferencesManager.getInstance(this).getUser().getID();
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        newPasswordConfirmation = (EditText) findViewById(R.id.newPasswordConfirmation);
        availabilityToggle = (ToggleButton) findViewById(R.id.availabilityToggle);
        changePasswordButton = (Button) findViewById(R.id.buttonChange);

        availabilityToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // you might keep a reference to the CheckBox to avoid this class cast
                boolean checked = ((ToggleButton) v).isChecked();
                toggleAvailability(checked);
            }

        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void toggleAvailability(final boolean status) {
        class ToggleAvailability extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);

                try {

                    JSONObject obj = new JSONObject(s);
                    Log.e("JSONOBJEsi:  ", obj.toString(4));
                    //if no error in response

                    Log.e("JSONERROR:  ", obj.getString("error"));

                    if (obj.getString("error").equals("FALSE")) {
                        Toast.makeText(getApplicationContext(), "Your status is updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        availabilityToggle.setChecked(!status);
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    availabilityToggle.setChecked(!status);
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("uid", "" + uid);
                params.put("status", status ? "1" : "0");
                //returing the response
                //TODO:Update this after toggle availability endpoint is ready
                return requestHandler.sendPostRequest(Constants.URL_Login, params);

            }
        }

        ToggleAvailability ta = new ToggleAvailability();
        ta.execute();
    }

    private void changePassword() {

        final String oldPass = oldPassword.getText().toString();
        final String newPass = newPassword.getText().toString();
        final String newPassConfirm = newPasswordConfirmation.getText().toString();
        newPassword.setError("These passwords should match");
        newPasswordConfirmation.setError("These passwords should match");


        if (newPass.equals(newPassConfirm)) {
            class ChangePassword extends AsyncTask<Void, Void, String> {

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

                        JSONObject obj = new JSONObject(s);
                        Log.e("JSONOBJEsi:  ", obj.toString(4));
                        //if no error in response

                        Log.e("JSONERROR:  ", obj.getString("error"));

                        if (obj.getString("error").equals("FALSE")) {
                            Toast.makeText(getApplicationContext(), "Your password is updated successfully", Toast.LENGTH_SHORT).show();
                            oldPassword.setText("");
                            newPassword.setText("");
                            newPasswordConfirmation.setText("");
                            newPassword.setError("");
                            newPasswordConfirmation.setError("");
                        } else {
                            oldPassword.setError(obj.getString("error_msg"));
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
                    params.put("oldPassword", oldPass);
                    params.put("newPassword", newPass);
//                    //returing the response
                    //TODO:Update this after change password endpoint is ready
                    return requestHandler.sendPostRequest(Constants.URL_Login, params);

                }
            }

            ChangePassword cp = new ChangePassword();
            cp.execute();


        } else {
            newPassword.setError("These passwords should match");
            newPasswordConfirmation.setError("These passwords should match");
            newPassword.requestFocus();
        }


    }

}

package com.bloodhub.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bloodhub.android.R;
import com.bloodhub.android.SharedPreferencesManager;


public class BaseActivity extends Activity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    protected void onCreateDrawer(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerList.setClickable(true);
        mDrawerList.addFooterView(((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.drawer_footer, null, false));
        addDrawerItems();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        // Set actionBarDrawerToggle as the DrawerListener
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);


        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


    }

    private void addDrawerItems() {
        String[] drawerItems = {"Home", "My Alerts", "Emergency 5 List", "Received Notifications", "Create New Blood Alert", "Homepage", "Blog", "Event Map"};
        mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_item, drawerItems);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("asda", "onItemClick: " + i);
                switch (i) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(), mySentNotificationActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), EmergencyFiveListActivity.class));

                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), myReceivedNotificationActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getApplicationContext(), CreateNotificationActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        break;
                    case 6:
                        startActivity(new Intent(getApplicationContext(), Blog.class));
                        break;
                    case 7:
                        startActivity(new Intent(getApplicationContext(), EventMap.class));
                        break;

                    default:
                }
            }
        });

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.drawer_logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Log.d("asdasf", "onClick: asd");
                SharedPreferencesManager.getInstance(getApplicationContext()).logout();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}

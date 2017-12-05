package com.example.gsimsek13.a28ekimfinalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.ArrayList;

public class DriverMainActivity extends AppCompatActivity {

    private DrawerLayout driver_drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition = 1;

    private ListView drawerList;

    //private String mLastUpdateTime;


    ArrayList<String> bufferStringArrayList;
    String[] driverStringArray;
    Bundle mySavedInstanceState;



    private class DriverDrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);


        //mLastUpdateTime = "";


        mySavedInstanceState = savedInstanceState;
        bufferStringArrayList = new ArrayList<String>();

        //titles = getResources().getStringArray(R.array.titles);
        drawerList = (ListView) findViewById(R.id.driver_drawer);
        driver_drawerLayout = (DrawerLayout) findViewById(R.id.driver_drawer_layout);

        driverStringArray = new String[]{ "My TimeList","QR Payment", "Offline Payment"};


        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, driverStringArray));
        drawerList.setOnItemClickListener(new DriverMainActivity.DriverDrawerItemClickListener());

        if (mySavedInstanceState != null) {
            currentPosition = mySavedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            selectItem(0);
        }

        drawerToggle = new ActionBarDrawerToggle(this, driver_drawerLayout,
                R.string.open_drawer, R.string.close_drawer)  {
            //Called when a drawer has settled in a completely closed state
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            //Called when a drawer has settled in a completely open state.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        driver_drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();


    }




    private void selectItem(int position) {

        currentPosition = position;
        android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();

        if(position== 0){

            FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.driver_content_frame);
            contentFrameLayout.removeAllViews();

            DriverTimeListFragment driverTimeListFragment = new DriverTimeListFragment();

            fragmentManager2.beginTransaction()
                    .replace(R.id.driver_content_frame,driverTimeListFragment,"visible_fragment")
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();


            

        } else if(position== 1){


            FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.driver_content_frame);
            contentFrameLayout.removeAllViews();

            TakePaymentFragment takePaymentFragment = new TakePaymentFragment();

            fragmentManager2.beginTransaction()
                    .replace(R.id.driver_content_frame,takePaymentFragment,"visible_fragment")
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();



        }
       else if(position == 2) {

            FrameLayout contentFrameLayout = (FrameLayout) findViewById((R.id.driver_content_frame));
            contentFrameLayout.removeAllViews();

            OfflineTakePaymentFragment offlineTakePaymentFragment = new OfflineTakePaymentFragment();
            fragmentManager2.beginTransaction()
                    .replace(R.id.driver_content_frame,offlineTakePaymentFragment,"visible_fragment")
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
        else{
            FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.driver_content_frame);
            contentFrameLayout.removeAllViews();
        }

        //Set the action bar title
        setActionBarTitle(position);
        //Close drawer

        driver_drawerLayout.closeDrawer(drawerList);
    }


    private void setActionBarTitle(int position) {
        String title;
        if (position == -1) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = driverStringArray[position];
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = driver_drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.action_share).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //  drawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver, menu);
        /*MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setIntent("This is example text");*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

          /*  case R.id.action_create_order:
                //Code to run when the Create Order item is clicked
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_check_out:
                Intent intent2 = new Intent(this, CheckActivity.class);
                startActivity(intent2);
                return true;*/
            case R.id.action_go_back:
                //Code to run when the settings item is clicked
              //  Fragment fragment = new ChangePasswordFragment();
               /* FrameLayout contentFrameLayout =  findViewById(R.id.driver_content_frame);
                contentFrameLayout.removeAllViews();

                TakePaymentFragment takePaymentFragment = new TakePaymentFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.driver_content_frame,takePaymentFragment,"visible_fragment")
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();*/



                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            //  default:
            //    return super.onOptionsItemSelected(item);
        }
        return true; // gecici
    }



}

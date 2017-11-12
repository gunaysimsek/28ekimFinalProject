package com.example.gsimsek13.a28ekimfinalproject;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class CustomerMainActivity extends AppCompatActivity   {

    private ShareActionProvider shareActionProvider;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition = 1;
    private String[] titles;
    private ListView drawerList;

    ArrayList<String> bufferStringArrayList;
    String[] myStringArray;
    Bundle mySavedInstanceState;

    TextView userName ;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        mySavedInstanceState = savedInstanceState;
        bufferStringArrayList = new ArrayList<String>();

        titles = getResources().getStringArray(R.array.titles);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        myStringArray = new String[]{"Profile", "QR","Reservation", "Schedule"};
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, myStringArray));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (mySavedInstanceState != null) {
            currentPosition = mySavedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            selectItem(0);
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
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
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();


       // Toast.makeText(getApplicationContext(), getIntent().getStringExtra("deneme"), Toast.LENGTH_SHORT).show();
     /*   userName = (TextView) findViewById(R.id.myUserName);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            userName.setText(name + ", " + email + ", " + photoUrl + ", " + uid + "." );
        }*/

    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        currentPosition = position;
        android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
        // Fragment fragment;
       /* switch (position) {
            case 0:

                StartersFragment.catNum = 0;
                break;
            case 1:
                fragment = new StartersFragment();
                StartersFragment.catNum = 1;
                break;
            case 2:
                fragment = new StartersFragment();
                StartersFragment.catNum = 2;
                break;
            case 3:
                fragment = new StartersFragment();
                StartersFragment.catNum = 3;
                break;

            default:
                fragment = new StartersFragment();
        }*/

            if(position== 0){

            FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
            contentFrameLayout.removeAllViews();

            ProfileFragment profileFrag = new ProfileFragment();
           // profileFrag.user = myUser;
            fragmentManager2.beginTransaction()
                    .replace(R.id.content_frame,profileFrag,"visible_fragment")
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        } else if(position== 1){

            Intent intent = new Intent(getApplicationContext(), QRActivity.class);

            startActivity(intent);


        }
         else if(position == 2) {

            FrameLayout contentFrameLayout = (FrameLayout) findViewById((R.id.content_frame));
            contentFrameLayout.removeAllViews();


            ReservationFragment reservationFrag = new ReservationFragment();
            fragmentManager2.beginTransaction()
                    .replace(R.id.content_frame,reservationFrag,"visible_fragment")
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            }
        else{
            FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
            contentFrameLayout.removeAllViews();
        }

        //Set the action bar title
        setActionBarTitle(position);
        //Close drawer

        drawerLayout.closeDrawer(drawerList);
    }

    private void setActionBarTitle(int position) {
        String title;
        if (position == -1) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = myStringArray[position];
        }
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);
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
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        /*MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setIntent("This is example text");*/
        return super.onCreateOptionsMenu(menu);
    }

    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        //  shareActionProvider.setShareIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        /*switch (item.getItemId()) {

            case R.id.action_create_order:
                //Code to run when the Create Order item is clicked
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_check_out:
                Intent intent2 = new Intent(this, CheckActivity.class);
                startActivity(intent2);
                return true;
            case R.id.action_settings:
                //Code to run when the settings item is clicked
                return true;
            case R.id.logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return true; // gecici
    }



    public void goQR(View view){

        Intent intent = new Intent(this, QRActivity.class);

        startActivity(intent);

    }
}
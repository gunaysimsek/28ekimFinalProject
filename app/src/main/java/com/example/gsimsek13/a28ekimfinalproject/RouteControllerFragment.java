package com.example.gsimsek13.a28ekimfinalproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Collections;




/**
 * A simple {@link Fragment} subclass.
 */

//yan cevirince crash
public class RouteControllerFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static String drivefrom;
    public static String driveto;
    public static String drivetime;
    Button routeStartButton;
    Button routeCancelButton;
    Button routeStopButton;
    RelativeLayout routeControllerRelative;
    FrameLayout routeControllerFrame;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private String[] parts;
    DriverRoutes driverRoutes;
    Calendar calender;
    boolean isWeekday = true;

    Times userTimes;
    Customer customerUpdated;


    protected static final String TAG = "==================";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    private final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private final static String LOCATION_KEY = "location-key";
    //private final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 10;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private Boolean mRequestingLocationUpdates;

    public RouteControllerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_controller, container, false);

        routeStartButton = (Button) v.findViewById(R.id.routeStartButton);
        routeCancelButton = (Button) v.findViewById(R.id.routeCancelButton);
        routeStopButton = (Button) v.findViewById(R.id.routeStopButton);
        routeControllerRelative = (RelativeLayout) v.findViewById(R.id.routeControllerRelative);
        routeControllerFrame = (FrameLayout) v.findViewById(R.id.routeControllerFrame) ;




        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");
        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        if((day!=7 && day !=1)){
            isWeekday = true;
        }else{
            isWeekday = false;
        }

        routeStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        driverRoutes = dataSnapshot.getValue(DriverRoutes.class);
                        if(isWeekday){
                            if(!driverRoutes.getWeekdayTimeListValue(drivetime).equalsIgnoreCase("cancelled")){
                                driverRoutes.setWeekdayTimeListValue(drivetime,"active");
                                myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Toast.makeText(getContext(), "Route is now active.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                startLocationUpdates();
                            }else{
                                Toast.makeText(getContext(), "Route is already cancelled.", Toast.LENGTH_SHORT).show();
                            }

                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            if(!driverRoutes.getWeekendTimeListValue(drivetime).equalsIgnoreCase("cancelled")){
                                driverRoutes.setWeekendTimeListValue(drivetime,"active");
                                myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Toast.makeText(getContext(), "Route is now active.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                startLocationUpdates();
                            }else{
                                Toast.makeText(getContext(), "Route is already cancelled.", Toast.LENGTH_SHORT).show();
                            }

                        }




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        routeCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        driverRoutes = dataSnapshot.getValue(DriverRoutes.class);


                        if(isWeekday) {
                            if (!driverRoutes.getWeekdayTimeListValue(drivetime).equalsIgnoreCase("cancelled")) {


                            driverRoutes.setWeekdayTimeListValue(drivetime, "cancelled");

                            myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekdayTimes").child(drivetime).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    userTimes = dataSnapshot.getValue(Times.class);
                                    Log.wtf("Price ===>", userTimes.toString());
                                    final double priceRetrieved = userTimes.getPrice();
                                    if (userTimes.getUsers() != null) {


                                        for (String user : userTimes.getUsers().keySet()) {
                                            final String currentUser = user;
                                            myRef.child("Customers").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    customerUpdated = dataSnapshot.getValue(Customer.class);
                                                    double updatedBalance = customerUpdated.getBalance() + priceRetrieved;
                                                    dataSnapshot.getRef().child("balance").setValue(updatedBalance);
                                                    myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekdayTimes").child(drivetime).child("users").child(currentUser).removeValue();
                                                    myRef.child("Customers").child(currentUser).child("reservations").child(drivefrom+"-"+driveto).child("times").child(drivetime).removeValue();
                                                    myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekdayTimes").child(drivetime).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.getChildrenCount() == 0) {
                                                                myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekdayTimes").child(drivetime).removeValue();
                                                            }
                                                            myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom + "-" + driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    Toast.makeText(getContext(), "Route is now cancelled.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            //Log.wtf("ASDDFASDFASDF===>",dataSnapshot.getChildrenCount());
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    // if(){
                                                    //   myRef.child("Routes").child(drivefrom+"-"+driveto).child("weekdayTimes").child(drivetime).removeValue();
                                                    //}
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    } else {
                                        if (!driverRoutes.getWeekdayTimeListValue(drivetime).equalsIgnoreCase("cancelled")) {
                                            myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekdayTimes").child(drivetime).removeValue();
                                            Toast.makeText(getContext(), "Route is now cancelled.", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                                Toast.makeText(getContext(), "Route is already cancelled.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            if (!driverRoutes.getWeekendTimeListValue(drivetime).equalsIgnoreCase("cancelled")) {

                            driverRoutes.setWeekendTimeListValue(drivetime, "cancelled");

                            myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekendTimes").child(drivetime).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    userTimes = dataSnapshot.getValue(Times.class);
                                    Log.wtf("Price ===>", userTimes.toString());
                                    final double priceRetrieved = userTimes.getPrice();
                                    if (userTimes.getUsers() != null) {


                                        for (String user : userTimes.getUsers().keySet()) {
                                            final String currentUser = user;
                                            myRef.child("Customers").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    customerUpdated = dataSnapshot.getValue(Customer.class);
                                                    double updatedBalance = customerUpdated.getBalance() + priceRetrieved;
                                                    dataSnapshot.getRef().child("balance").setValue(updatedBalance);
                                                    myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekendTimes").child(drivetime).child("users").child(currentUser).removeValue();

                                                    myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekendTimes").child(drivetime).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.getChildrenCount() == 0) {
                                                                myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekendTimes").child(drivetime).removeValue();
                                                            }
                                                            myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom + "-" + driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                    Toast.makeText(getContext(), "Route is now cancelled.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            //Log.wtf("ASDDFASDFASDF===>",dataSnapshot.getChildrenCount());
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                    // if(){
                                                    //   myRef.child("Routes").child(drivefrom+"-"+driveto).child("weekdayTimes").child(drivetime).removeValue();
                                                    //}
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    } else {
                                        if (!driverRoutes.getWeekendTimeListValue(drivetime).equalsIgnoreCase("cancelled")) {
                                        myRef.child("Routes").child(drivefrom + "-" + driveto).child("weekdayTimes").child(drivetime).removeValue();
                                        Toast.makeText(getContext(), "Route is now cancelled.", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else{
                                Toast.makeText(getContext(), "Route is already cancelled.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        /*
                        myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(getContext(), "Route is now cancelled.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        */
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        routeStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        driverRoutes = dataSnapshot.getValue(DriverRoutes.class);
                        if(isWeekday){
                            if(!driverRoutes.getWeekdayTimeListValue(drivetime).equalsIgnoreCase("cancelled")){
                                driverRoutes.setWeekdayTimeListValue(drivetime,"ended");
                                myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Toast.makeText(getContext(), "Route is now ended.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                stopLocationUpdates();
                            }else{
                                Toast.makeText(getContext(), "Route is already cancelled.", Toast.LENGTH_SHORT).show();
                            }
                            //driverRoutes.setWeekdayTimeListValue(drivetime,"ended");
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            if(!driverRoutes.getWeekendTimeListValue(drivetime).equalsIgnoreCase("cancelled")){
                                driverRoutes.setWeekendTimeListValue(drivetime,"ended");
                                myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Toast.makeText(getContext(), "Route is now ended.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                stopLocationUpdates();
                            }else{
                                Toast.makeText(getContext(), "Route is already cancelled.", Toast.LENGTH_SHORT).show();
                            }
                            //driverRoutes.setWeekendTimeListValue(drivetime,"ended");
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        mRequestingLocationUpdates = false;
        //mLastUpdateTime = "";

        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();

        // Inflate the layout for this fragment
        return v;
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        //Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);

            }

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
            //if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
            //    mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            //}

        }
    }
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (!isPlayServicesAvailable(getContext())) return;
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
        } else {
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {
            startLocationUpdates();
            return;
        }
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRationaleDialog();
            } else {
                //showRationaleDialog();
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }




    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,RouteControllerFragment.this);
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                        mRequestingLocationUpdates = false;
                    } else {
                        showRationaleDialog();
                    }
                }
                break;
            }
        }
    }

    private void showRationaleDialog() {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                })
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRequestingLocationUpdates = false;
                    }
                })
                .setCancelable(true)
                .setMessage("Access to location services is needed. Please update your settings")
                .show();
    }

    public static boolean isPlayServicesAvailable(Context context) {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog((Activity) context, resultCode, 2).show();
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        showRationaleDialog();
                        break;
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        //if(getActivity()!=null)
         //   getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        isPlayServicesAvailable(getContext());



        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        stopLocationUpdates();
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.wtf("MERHABA BURADAYIM","SLM");
        //mCurrentLocation = location;
        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        //Toast.makeText(this, "location updated", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Latitude==="+mCurrentLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        // Toast.makeText(this, "Longtitude==="+mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        //Log.i("SELAM","Latitude=== "+mCurrentLocation.getLatitude());
        //Log.i("SELAM2","Longtitude=== "+mCurrentLocation.getLongitude());

        mCurrentLocation = location;


        //Place current location marker
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        String[] parts2 = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");


        myRef.child("Drivers").child(parts2[0]).addListenerForSingleValueEvent(new ValueEventListener() { //burdaki child yerine hangi driveri istiyosak onu yazicaz
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("latitude").setValue(mCurrentLocation.getLatitude());
                dataSnapshot.getRef().child("longitude").setValue(mCurrentLocation.getLongitude());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        //move map camera

    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        //savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }




}

package com.example.gsimsek13.a28ekimfinalproject;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverFinderFragment extends Fragment {
    Spinner toSpin;
    Spinner fromSpin;
    Spinner timeSpin;
    Button findDriver;
    GridLayout find_driver_gridlayout;
    FrameLayout find_driver_frame;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Route route;


    public DriverFinderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_finder,container,false );


        findDriver = (Button) v.findViewById(R.id.findDriverButton);
        toSpin = (Spinner) v.findViewById(R.id.toSpin);
        fromSpin = (Spinner) v.findViewById(R.id.fromSpin);
        timeSpin = (Spinner) v.findViewById(R.id.timeSpin);
        find_driver_gridlayout = (GridLayout) v.findViewById(R.id.find_draver_gridlayout);
        find_driver_frame = (FrameLayout) v.findViewById(R.id.driver_frame) ;



        List<String> spinnerArray =  new ArrayList<String>();


        /*
        myRef.child("Routes").addValueEventListener(new ValueEventListener() { //BURASI DEGISICEK
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                route  =  dataSnapshot.getValue(Route.class);
                LatLng latLng = new LatLng(driver.getLatitude(), driver.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Shuttle");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mDriverLocationMarker = mMap.addMarker(markerOptions);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        */

        spinnerArray.add("item1");
        spinnerArray.add("item2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) v.findViewById(R.id.fromSpin);
        sItems.setAdapter(adapter);




        findDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fromSpin.toString().isEmpty()||toSpin.toString().isEmpty()||timeSpin.toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Fill Above Fields", Toast.LENGTH_SHORT).show();
                }else{
                    String toSpinValue = toSpin.getSelectedItem().toString();
                    String fromSpinValue = fromSpin.getSelectedItem().toString();
                    String timeSpinValue = timeSpin.getSelectedItem().toString();
                }

                find_driver_frame.removeAllViews();
                LocationFragment newFragment = new LocationFragment();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.driver_frame, newFragment);
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        });
        return v;
    }

}

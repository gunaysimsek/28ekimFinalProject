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
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */

//TIME SPINNER HALLEDILECEK
    // ONCE FROM SPINNER SEC SONRA ONA GORE TO SPINNER EN SON IKISINE GORE TIMESPINNER
    // DRIVER LOCATION ONA GORE UPDATE EDILECEK
    // FOCUS DRIVER FOCUS CUSTOMER BUTTONLARI EKLENECEK
public class DriverFinderFragment extends Fragment {
    Spinner toSpin;
    Spinner fromSpin;
    Spinner timeSpin;
    Button findDriver;
    //List<String> fromArray =  new ArrayList<String>();
    //List<String> toArray =  new ArrayList<String>();
    List<String> timeArray =  new ArrayList<String>();
    GridLayout find_driver_gridlayout;
    FrameLayout find_driver_frame;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Route route;
    Calendar calender;
    boolean isWeekday = true;


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

        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if((day!=7 || day !=1)){
            isWeekday = true;
        }else{
            isWeekday = false;
        }


        myRef.child("Routes").addValueEventListener(new ValueEventListener() { //BURASI DEGISICEK
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final List<String> fromList = new ArrayList<String>();
                Set<String> fromSet = new HashSet<>();
                //final List<String> toList = new ArrayList<String>();
                Set<String> toSet = new HashSet<>();
                for (DataSnapshot eachFromToSnapshot : dataSnapshot.getChildren()) {

                    String [] fromTo = eachFromToSnapshot.getKey().toString().split("-");
                    String from = fromTo[0];
                    String to = fromTo[1];
                    fromSet.add(from);
                    toSet.add(to);
                    Log.wtf("hELLOOGGGgggg",from);

                }
                List<String> fromList = new ArrayList<>(fromSet);
                List<String> toList = new ArrayList<>(toSet);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, fromList);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, toList);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



                fromSpin.setAdapter(adapter);
                toSpin.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HELLOOG", "Failed to read value.", databaseError.toException());
            }
        });


       /* for(int i=0;i<fromArray.size();i++){
            myRef.child("Routes").child(fromArray.get(i)+"-"+toArray.get(i)).addValueEventListener(new ValueEventListener() { //BURASI DEGISICEK
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    route  =  dataSnapshot.getValue(Route.class);

                    if(isWeekday){
                        timeArray.addAll(route.getWeekdayTimes().keySet());
                    }else{
                        timeArray.addAll(route.getWeekendTimes().keySet());
                    }
                    Log.wtf("hELLOOGGG","SELAM");
                    /*Log.wtf("HELLO1",route.getFrom());
                    Log.wtf("HELLO1",route.getTo());
                    Log.wtf("HELLO1",route.toString());
                    Log.wtf("HELLO1",route.getWeekdayTimesValue("19:45").getDriver());
                    Log.wtf("HELLO1",route.getWeekdayTimesValue("19:45").toString());
                    Log.wtf("HELLO1",route.getWeekdayTimesValue("19:45").getCustomerValue("emadran13"));
                    Log.wtf("HELLO1",route.getWeekdayTimesValue("19:45").getUsers().keySet().toString());
                    Log.wtf("HELLO1",route.getWeekdayTimes().keySet().toString());
                    Log.wtf("HELLO1",route.getWeekendTimes().keySet().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("HELLOOG", "Failed to read value.", databaseError.toException());
                }
            });
        }
        */

        //Log.wtf("hELLOOGGG",fromArray.toString());
        Log.wtf("hELLOOGGG",timeArray.toString());

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        //        getContext(), android.R.layout.simple_spinner_item, fromArray);
        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
        //        getContext(), android.R.layout.simple_spinner_item, toArray);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, timeArray);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Spinner sItems = (Spinner) v.findViewById(R.id.fromSpin);
        //Spinner sItems2 = (Spinner) v.findViewById(R.id.toSpin);
        Spinner sItems3 = (Spinner) v.findViewById(R.id.timeSpin);

        //sItems.setAdapter(adapter);
        //sItems2.setAdapter(adapter2);
        sItems3.setAdapter(adapter3);




        findDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if(fromSpin.toString().isEmpty()||toSpin.toString().isEmpty()||timeSpin.toString().isEmpty()){
                    Toast.makeText(getContext(), "Please Fill Above Fields", Toast.LENGTH_SHORT).show();
                }else{
                    String toSpinValue = toSpin.getSelectedItem().toString();
                    String fromSpinValue = fromSpin.getSelectedItem().toString();
                    String timeSpinValue = timeSpin.getSelectedItem().toString();
                }*/

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

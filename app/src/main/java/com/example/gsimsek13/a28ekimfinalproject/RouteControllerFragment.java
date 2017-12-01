package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
public class RouteControllerFragment extends Fragment {


    // route un durumuna gore driver location update i burda olucak.
    // location fragmentda ended ise shuttle in yeri gozukmeyecek.
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
                           driverRoutes.setWeekdayTimeListValue(drivetime,"active");
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            driverRoutes.setWeekendTimeListValue(drivetime,"active");
                        }

                        myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(getContext(), "Route is now active.", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                        if(isWeekday){
                            driverRoutes.setWeekdayTimeListValue(drivetime,"cancelled");
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            driverRoutes.setWeekendTimeListValue(drivetime,"cancelled");
                        }
                        myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(getContext(), "Route is now cancelled.", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                            driverRoutes.setWeekdayTimeListValue(drivetime,"ended");
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            driverRoutes.setWeekendTimeListValue(drivetime,"ended");
                        }
                        myRef.child("Drivers").child(parts[0]).child("routes").child(drivefrom+"-"+driveto).setValue(driverRoutes, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(getContext(), "Route is now ended.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        // Inflate the layout for this fragment
        return v;
    }




}

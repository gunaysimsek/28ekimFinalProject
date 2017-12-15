package com.example.gsimsek13.a28ekimfinalproject;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class AnnouncementFragment extends Fragment {

    ListView announcementListView;
    RelativeLayout announcementRelative;
    FrameLayout announcementFrameLayout;

    //List<String> fromArray =  new ArrayList<String>();
    //List<String> toArray =  new ArrayList<String>();
    //List<String> timeArray =  new ArrayList<String>();

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<String> cancelledRoutes;
    Calendar calender;
    boolean isWeekday = true;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_announcement, container, false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        announcementListView = (ListView) v.findViewById(R.id.announcementListView);
        announcementRelative = (RelativeLayout) v.findViewById(R.id.announcementRelative);
        announcementFrameLayout = (FrameLayout) v.findViewById(R.id.announcementFrameLayout) ;
        cancelledRoutes = new ArrayList<>();
        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if((day!=7 && day !=1)){
            isWeekday = true;
        }else{
            isWeekday = false;
        }

        myRef.child("Drivers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot eachChild : dataSnapshot.getChildren()){
                    Driver eachDriver = eachChild.getValue(Driver.class);
                   for(String routes : eachDriver.getRoutes().keySet()){
                        DriverRoutes driverRoutes = eachDriver.getRouteValue(routes);
                        if(isWeekday){
                            for(String times : driverRoutes.getWeekdayTimeList().keySet()){
                                String checker = driverRoutes.getWeekdayTimeListValue(times);

                                if(checker.equalsIgnoreCase("cancelled")){
                                    String output = routes+" "+times;
                                    cancelledRoutes.add(output);
                                }
                            }
                        }else{
                            for(String times : driverRoutes.getWeekendTimeList().keySet()){
                                String checker = driverRoutes.getWeekendTimeListValue(times);
                                if(checker.equalsIgnoreCase("cancelled")){
                                    String output = routes+" "+times;
                                    cancelledRoutes.add(output);
                                }
                            }
                        }
                   }
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        cancelledRoutes ){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        // Get the Item from ListView
                        View view = super.getView(position, convertView, parent);

                        // Initialize a TextView for ListView each Item
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);

                        // Set the text color of TextView (ListView Item)
                        //tv.setTextColor(Color.YELLOW);
                        tv.setTextColor(Color.WHITE);
                        if(position % 3 == 0){
                            tv.setBackgroundColor(Color.parseColor("#0F7E1C"));
                        }else if(position % 3 == 1){
                            tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }else{
                            tv.setBackgroundColor(Color.parseColor("#00CED1"));
                        }


                        // Generate ListView Item using TextView
                        return view;
                    }
                };

                announcementListView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return v;
    }




}

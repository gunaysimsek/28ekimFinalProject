package com.example.gsimsek13.a28ekimfinalproject;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */

public class DriverFinderFragment extends Fragment {
    Spinner toSpin;
    Spinner fromSpin;
    Spinner timeSpin;
    Button findDriver;
    //List<String> fromArray =  new ArrayList<String>();
    //List<String> toArray =  new ArrayList<String>();
    //List<String> timeArray =  new ArrayList<String>();
    android.support.v7.widget.GridLayout find_driver_gridlayout;
    FrameLayout find_driver_frame;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Route route;
    Calendar calender;
    String driverN;
    String fromN;
    String toN;
    String timeN;
    String timeIntervalN;
    boolean isWeekday = true;
    Times times;
    HashMap<String,ArrayList<String>> fromToTable = new HashMap<>();



    public DriverFinderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_finder,container,false );

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        findDriver = (Button) v.findViewById(R.id.findDriverButton);
        toSpin = (Spinner) v.findViewById(R.id.toSpin);
        fromSpin = (Spinner) v.findViewById(R.id.fromSpin);
        timeSpin = (Spinner) v.findViewById(R.id.timeSpin);
        find_driver_gridlayout = (android.support.v7.widget.GridLayout) v.findViewById(R.id.find_draver_gridlayout);
        find_driver_frame = (FrameLayout) v.findViewById(R.id.driver_frame) ;

        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if((day!=7 && day !=1)){
            isWeekday = true;
            timeIntervalN = "weekdayTimeList";
        }else{
            isWeekday = false;
            timeIntervalN = "weekendTimeList";
        }


        myRef.child("Routes").addListenerForSingleValueEvent(new ValueEventListener() { //BURASI DEGISICEK
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final List<String> fromList = new ArrayList<String>();
                Set<String> fromSet = new HashSet<>();

                // final HashMap<String, ArrayList<String>> fromToTable = new HashMap<>();

                //final HashMap<String,ArrayList<String>> fromToTable = new HashMap<>();

                //final List<String> toList = new ArrayList<String>();
                //Set<String> toSet = new HashSet<>();
                for (DataSnapshot eachFromToSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> to = new ArrayList<>();
                    String [] fromTo = eachFromToSnapshot.getKey().toString().split("-");
                    //Log.wtf("yeter",fromTo[0]+" "+fromTo[1]);
                    String from = fromTo[0];
                    fromSet.add(from);
                    /*Log.wtf("to nedir=?",to.toString());
                    to.clear();
                    Log.wtf("to simdi nedir=?",to.toString());
                    to.add(fromTo[1]);
                    Log.wtf("to asil simdi nedir=?",to.toString());
                    Log.wtf("from asil simdi nedir=?",from);
                    fromToTable.put(from,to);*/

                   if(fromToTable.get(from)!=null){
                        //to.removeAll(to);
                        to = fromToTable.get(from);
                        to.add(fromTo[1]);
                        //Log.wtf("onlyoncebabe",from);
                        //Log.wtf("onlyoncebabae",to.toString());
                        fromToTable.put(from,to);

                        //Log.wtf("HOHOHOHO2",fromToTable.get(from).toString());
                        //Log.wtf("HOHOHO2",to.toString());
                    }else{
                        //to.removeAll(to);
                        to.add(fromTo[1]);
                        //Log.wtf("ANAAAAAAAAAAAAAAAAAAAAAA",from);
                        //Log.wtf("BABBABABABAAAAAAAAAAAAAA",to.toString());
                        fromToTable.put(from,to);

                    }

                    //Log.wtf("aaaaaaaaaaaaaaaaaaaaaaaa",fromToTable.keySet().toString());
                    //Log.wtf("HOHOHO======"+fromToTable.get(from).size(),"asdasdfasd");

                    //Log.wtf("HOHOHOHO2",fromToTable.get(from).toString());


                    //fromToTable.put(from,to);

                    //toSet.add(to);
                    //Log.wtf("hELLOOGGGgggg",from);


                }
                /*for(String a : fromToTable.keySet()){
                    Log.wtf("lolololololololo",a);
                    Log.wtf("hahahahahahaha",fromToTable.get(a).toString());
                }*/


                if(fromToTable.get("Ana Kampus")!=null)
                    Log.wtf("Ana Kampusten Nereye?",fromToTable.get("Ana Kampus").toString());
                if(fromToTable.get("Bati")!=null)
                    Log.wtf("Batidan Nereye?",fromToTable.get("Bati").toString());
                if(fromToTable.get("Haciosman")!=null)
                    Log.wtf("Haciosmandan Nereye?",fromToTable.get("Haciosman").toString());

                List<String> fromList = new ArrayList<>(fromSet);
                //List<String> toList = new ArrayList<>(toSet);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, fromList);
                //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                 //       getContext(), android.R.layout.simple_spinner_item, toList);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



                fromSpin.setAdapter(adapter);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HELLOOG", "Failed to read value.", databaseError.toException());
            }
        });




        fromSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = fromSpin.getSelectedItem().toString();
                //Log.wtf("TEXT DEGISTIII",text);
                //Log.wtf("TEXT DEGISTIII",fromToTable.get(text).toString());
                //Log.wtf("TEXT DEGISTIII========="+fromToTable.get(text).size(),"hohoho");
                //Log.wtf("asdfasdf",fromToTable.get(text).toString());

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, fromToTable.get(text));

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter2.notifyDataSetChanged();
                toSpin.setAdapter(adapter2);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //toSpin.setAdapter(adapter2);




        toSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text1 = fromSpin.getSelectedItem().toString();
                String text2 = toSpin.getSelectedItem().toString();
                //Log.wtf("TEXT DEGISTIII",text);
                //Log.wtf("TEXT DEGISTIII",fromToTable.get(text).toString());
                //Log.wtf("TEXT DEGISTIII========="+fromToTable.get(text).size(),"hohoho");
                //Log.wtf("asdfasdf",fromToTable.get(text).toString());
                myRef.child("Routes").child(text1+"-"+text2).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        route = dataSnapshot.getValue(Route.class);
                        ArrayList<String> timeList = new ArrayList<>();

                        if(isWeekday){
                            timeList.addAll(route.getWeekdayTimes().keySet());
                            Collections.sort(timeList);
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            timeList.addAll(route.getWeekendTimes().keySet());
                            Collections.sort(timeList);
                        }
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                                getContext(), android.R.layout.simple_spinner_item, timeList);

                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        adapter3.notifyDataSetChanged();
                        timeSpin.setAdapter(adapter3);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        timeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text1 = fromSpin.getSelectedItem().toString();
                String text2 = toSpin.getSelectedItem().toString();
                String text3 = timeSpin.getSelectedItem().toString();

                fromN = text1;
                toN = text2;
                timeN = text3;
                //Log.wtf("TEXT DEGISTIII",text);
                //Log.wtf("TEXT DEGISTIII",fromToTable.get(text).toString());
                //Log.wtf("TEXT DEGISTIII========="+fromToTable.get(text).size(),"hohoho");
                //Log.wtf("asdfasdf",fromToTable.get(text).toString());
                if(isWeekday){
                    myRef.child("Routes").child(text1+"-"+text2).child("weekdayTimes").child(text3).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            times = dataSnapshot.getValue(Times.class);
                            Log.wtf("TIMES===>",times.toString());
                            driverN = times.driver;



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    myRef.child("Routes").child(text1+"-"+text2).child("weekendTimes").child(text3).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            times = dataSnapshot.getValue(Times.class);
                            Log.wtf("TIMES===>",times.toString());
                            driverN = times.driver;


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        //Log.wtf("hELLOOGGG",timeArray.toString());

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        //        getContext(), android.R.layout.simple_spinner_item, fromArray);
        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
        //        getContext(), android.R.layout.simple_spinner_item, toArray);
        //ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
        //        getContext(), android.R.layout.simple_spinner_item, timeArray);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Spinner sItems = (Spinner) v.findViewById(R.id.fromSpin);
        //Spinner sItems2 = (Spinner) v.findViewById(R.id.toSpin);
        //Spinner sItems3 = (Spinner) v.findViewById(R.id.timeSpin);

        //sItems.setAdapter(adapter);
        //sItems2.setAdapter(adapter2);
        //sItems3.setAdapter(adapter3);




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
                Fragment newFragment = new LocationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                LocationFragment.drivername = driverN;
                LocationFragment.fromname = fromN;
                LocationFragment.toname = toN;
                LocationFragment.timename = timeN;
                LocationFragment.timeintervalname = timeIntervalN;

                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // Commit the transaction
                transaction.commit();
            }
        });
        return v;
    }

}

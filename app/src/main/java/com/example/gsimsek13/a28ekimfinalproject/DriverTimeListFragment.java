package com.example.gsimsek13.a28ekimfinalproject;


import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverTimeListFragment extends Fragment {
    Spinner driverToSpin;
    Spinner driverFromSpin;
    Button driverTimeListButton;
    ListView driverTimeListView;

    //List<String> fromArray =  new ArrayList<String>();
    //List<String> toArray =  new ArrayList<String>();
    //List<String> timeArray =  new ArrayList<String>();

    RelativeLayout driverTimeListRelLayout;
    FrameLayout driverTimeListFrame;
    FirebaseDatabase database;
    DatabaseReference myRef;
    //Route route;
    Calendar calender;
    String driverN;
    boolean isWeekday = true;
    private String[] parts;
    Driver driver;
    DriverRoutes driverRoutes;
    //Times times;
    HashMap<String,ArrayList<String>> fromToTable = new HashMap<>();
    ArrayList<String> driverTimes;

    public DriverTimeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_time_list, container, false);
        // Inflate the layout for this fragment

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        driverTimeListButton = (Button) v.findViewById(R.id.driverTimeListButton);
        driverToSpin = (Spinner) v.findViewById(R.id.driverToSpin);
        driverFromSpin = (Spinner) v.findViewById(R.id.driverFromSpin);
        driverTimeListView = (ListView) v.findViewById(R.id.driverTimeListView);
        driverTimeListRelLayout = (RelativeLayout) v.findViewById(R.id.driverTimeListRelLayout);
        driverTimeListFrame = (FrameLayout) v.findViewById(R.id.driverTimeListFrame) ;
        driverTimes = new ArrayList<>();
        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if((day!=7 && day !=1)){
            isWeekday = true;
        }else{
            isWeekday = false;
        }
        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");

        myRef.child("Drivers").child(parts[0]).child("routes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final List<String> fromList = new ArrayList<String>();

                //driver  =  dataSnapshot.getValue(Driver.class);
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



                driverFromSpin.setAdapter(adapter);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("HELLOOG", "Failed to read value.", databaseError.toException());
            }
        });




        driverFromSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = driverFromSpin.getSelectedItem().toString();
                //Log.wtf("TEXT DEGISTIII",text);
                //Log.wtf("TEXT DEGISTIII",fromToTable.get(text).toString());
                //Log.wtf("TEXT DEGISTIII========="+fromToTable.get(text).size(),"hohoho");
                //Log.wtf("asdfasdf",fromToTable.get(text).toString());

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, fromToTable.get(text));

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter2.notifyDataSetChanged();
                driverToSpin.setAdapter(adapter2);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //toSpin.setAdapter(adapter2);



/*
        driverToSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text1 = driverFromSpin.getSelectedItem().toString();
                String text2 = driverToSpin.getSelectedItem().toString();
                //Log.wtf("TEXT DEGISTIII",text);
                //Log.wtf("TEXT DEGISTIII",fromToTable.get(text).toString());
                //Log.wtf("TEXT DEGISTIII========="+fromToTable.get(text).size(),"hohoho");
                //Log.wtf("asdfasdf",fromToTable.get(text).toString());
                myRef.child("Drivers").child(parts[0]).child("routes").child(text1+"-"+text2).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        driverRoutes = dataSnapshot.getValue(DriverRoutes.class);
                        ArrayList<String> timeList = new ArrayList<>();

                        if(isWeekday){
                            timeList.addAll(driverRoutes.getWeekdayTimeList().keySet());
                            Collections.sort(timeList);
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            timeList.addAll(driverRoutes.getWeekendTimeList().keySet());
                            Collections.sort(timeList);
                        }
                        driverTimes.addAll(timeList);

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
        */

        driverTimeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String selectedTime = driverTime.toString();
                String from = driverFromSpin.getSelectedItem().toString();
                String to = driverToSpin.getSelectedItem().toString();

                myRef.child("Drivers").child(parts[0]).child("routes").child(from+"-"+to).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        driverRoutes = dataSnapshot.getValue(DriverRoutes.class);
                        ArrayList<String> timeList = new ArrayList<>();

                        if(isWeekday){
                            timeList.removeAll(timeList);
                            for(String timeValue : driverRoutes.getWeekdayTimeList().keySet()){
                                if(!driverRoutes.getWeekdayTimeListValue(timeValue).equalsIgnoreCase("cancelled")||!driverRoutes.getWeekdayTimeListValue(timeValue).equalsIgnoreCase("ended")){
                                    timeList.add(timeValue);
                                }
                            }
                            //timeList.addAll(driverRoutes.getWeekdayTimeList().keySet());
                            Collections.sort(timeList);
                            //Log.wtf("adfasdaf",timeList.toString());
                        }else{
                            timeList.removeAll(timeList);
                            for(String timeValue : driverRoutes.getWeekendTimeList().keySet()){
                                if(!driverRoutes.getWeekendTimeListValue(timeValue).equalsIgnoreCase("cancelled")||!driverRoutes.getWeekendTimeListValue(timeValue).equalsIgnoreCase("ended")){
                                    timeList.add(timeValue);
                                }
                            }
                            //timeList.addAll(driverRoutes.getWeekendTimeList().keySet());
                            Collections.sort(timeList);
                        }
                        //driverTimes.addAll(timeList);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                getContext(),
                                android.R.layout.simple_list_item_1,
                                timeList ){
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

                        driverTimeListView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


        driverTimeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedTime = (driverTimeListView.getItemAtPosition(i)).toString();
                String from = driverFromSpin.getSelectedItem().toString();
                String to = driverToSpin.getSelectedItem().toString();
                //Log.wtf("asdfasdfa",selectedFromList);

                driverTimeListFrame.removeAllViews();
                Fragment newFragment = new RouteControllerFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                RouteControllerFragment.drivetime = selectedTime;
                RouteControllerFragment.drivefrom = from;
                RouteControllerFragment.driveto = to;

                Log.wtf("asdfsdfas",selectedTime);
                Log.wtf("asdfasdfasdfa",from);
                Log.wtf("asdfasdfasdfasdf",to);



                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.driver_content_frame, newFragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // Commit the transaction
                transaction.commit();

            }
        });






        return v;
    }

}

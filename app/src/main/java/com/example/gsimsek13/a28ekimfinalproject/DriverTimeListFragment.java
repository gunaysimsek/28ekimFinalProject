package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        myRef.child("Drivers").child(parts[0]).child("routes").addValueEventListener(new ValueEventListener() {
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




        driverToSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text1 = driverFromSpin.getSelectedItem().toString();
                String text2 = driverToSpin.getSelectedItem().toString();
                //Log.wtf("TEXT DEGISTIII",text);
                //Log.wtf("TEXT DEGISTIII",fromToTable.get(text).toString());
                //Log.wtf("TEXT DEGISTIII========="+fromToTable.get(text).size(),"hohoho");
                //Log.wtf("asdfasdf",fromToTable.get(text).toString());
                myRef.child("Drivers").child(parts[0]).child("routes").child(text1+"-"+text2).addValueEventListener(new ValueEventListener() {
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

        driverTimeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        driverTimes );

                driverTimeListView.setAdapter(arrayAdapter);

            }
        });
        






        return v;
    }

}

package com.example.gsimsek13.a28ekimfinalproject;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Bartu on 11/13/2017.
 */

public class ScheduleFragment extends Fragment{

    TextView CurrentTime;
    ArrayList<ArrayList<String>> returningList = new ArrayList<ArrayList<String>>();
    View mainView;
    int currentTimeHour;
    int currentTimeMinute;
    int currentTimeTotal;
    ArrayList<ArrayList<String>> returningScheduleList = new ArrayList<ArrayList<String>>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Route route;
    boolean isWeekday = true;
    Spinner Schedule_To_Spinnerr;
    HashMap<String,ArrayList<String>> fromToTable = new HashMap<>();
    Spinner Schedule_From_Spinnerr;
    Button goButton;

    LinearLayout scrollViewLinearLayout;
    ScrollView scheduleScrollView;
    Calendar calender;

    public ScheduleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Log.d("deneme","hopkiri");

        Thread myThread = null;

        Runnable myRunnableThread = new CountDownRunner();
        myThread= new Thread(myRunnableThread);
        myThread.start();


        Date dt = new Date();
        int hours = dt.getHours();
        currentTimeHour = hours;
        //Log.d("deneme", "hey " + hours);
        //Log.d("deneme", "hey " + currentTimeHour);
        int minutes = dt.getMinutes();
        currentTimeMinute = minutes;




        View v = inflater.inflate(R.layout.fragment_schedule,container,false );
        mainView = v;
        //Date currentTime = Calendar.getInstance().getTime();

        CurrentTime = (TextView) v.findViewById(R.id.Schedule_Text_View);
        /*
        leftLinearLayout = (LinearLayout) v.findViewById(R.id.Schedule_Left_Linear_Layout);
        rightLinearLayout = (LinearLayout) v.findViewById(R.id.Schedule_Right_Linear_Layout) ;
        centerLeftLinearLayout = (LinearLayout) v.findViewById(R.id.Schedule_Center_Left_Linear_Layout);
        centerRightLinearLayout = (LinearLayout) v.findViewById(R.id.Schedule_Center_Right_Linear_Layout);
        */
        scheduleScrollView = (ScrollView) v.findViewById(R.id.Schedule_Scroll_View);
        scrollViewLinearLayout = (LinearLayout) v.findViewById(R.id.Schedule_Scroll_View_Linear_Layout);

        Schedule_From_Spinnerr = (Spinner) v.findViewById(R.id.Schedule_From_Spinner);
        Schedule_To_Spinnerr = (Spinner) v.findViewById(R.id.Schedule_To_Spinner);
        goButton = (Button) v.findViewById(R.id.Schedule_Go_Button);



        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if((day!=7 && day !=1)){
            isWeekday = true;
        }else{
            isWeekday = false;
        }

        //String time = CurrentTime.getText().toString();
        //String[] seperatedTime = time.split(":");
        currentTimeTotal = currentTimeHour*60 + currentTimeMinute;
        //Log.d("deneme", "hey " + currentTimeTotal);
        //CurrentTime.setText(currentTime.toString());


        myRef.child("Routes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> fromSet = new HashSet<>();

                //returningScheduleList = new ArrayList<ArrayList<String>>();

                for (DataSnapshot eachFromToSnapshot : dataSnapshot.getChildren()) {


                    ArrayList<String> to = new ArrayList<>();
                    String [] fromTo = eachFromToSnapshot.getKey().toString().split("-");
                    //Log.wtf("yeter",fromTo[0]+" "+fromTo[1]);
                    String from = fromTo[0];
                    fromSet.add(from);


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


//                    String fromTo = eachFromToSnapshot.getKey().toString();
//                    String[] fromToParsed = fromTo.split("-");
//                    String from = fromToParsed[0];
//                    String to = fromToParsed[1];

                    /*
                    for (DataSnapshot singleSnapshot : eachFromToSnapshot.getChildren()) {

                        route = dataSnapshot.getValue(Route.class);
                        Log.d("Scheduleee",route.toString());
                        ArrayList<String> timeList = new ArrayList<>();
                        if(isWeekday){

                            timeList.addAll(route.getWeekdayTimes().keySet());
                            Collections.sort(timeList);
                            for(String eachtime : timeList) {
                                ArrayList<String> eachShuttle = new ArrayList<String>();

                                eachShuttle.add(from);
                                eachShuttle.add(to);

                                eachShuttle.add(eachtime);
                                int availability = route.getWeekdayTimesValue(eachtime).availability;
                                eachShuttle.add(String.valueOf(availability));
                                double price = route.getWeekdayTimesValue(eachtime).getPrice();
                                eachShuttle.add(String.valueOf(price)+ " TL");

                                if(eachShuttle.get(1).equals("0")){
                                    eachShuttle.add("No Button");
                                }
                                else {
                                    eachShuttle.add("Button");
                                }
                                eachShuttle.add("weekdayTimes");
                                returningScheduleList.add(eachShuttle);

                            }
                        }
                        else {
                            timeList.addAll(route.getWeekendTimes().keySet());
                            Collections.sort(timeList);

                            for(String eachtime : timeList) {
                                ArrayList<String> eachShuttle = new ArrayList<String>();
                                eachShuttle.add(eachtime);
                                int availability = route.getWeekdayTimesValue(eachtime).availability;
                                eachShuttle.add(String.valueOf(availability));
                                double price = route.getWeekdayTimesValue(eachtime).getPrice();
                                eachShuttle.add(String.valueOf(price)+ " TL");

                                if(eachShuttle.get(1).equals("0")){
                                    eachShuttle.add("No Button");
                                }
                                else {
                                    eachShuttle.add("Button");
                                }
                                eachShuttle.add("weekendTimes");
                                returningScheduleList.add(eachShuttle);

                            }
                        }
//
//                        String time = singleSnapshot.getKey().toString();
//                        eachShuttle.add(time);
//
//                        String availability = "";
//                        String price = "";
//                        for (DataSnapshot eachSnapshot : singleSnapshot.getChildren()) {
//
//                            if (eachSnapshot.getKey().equals("Price")) {
//                                price = eachSnapshot.getValue().toString();
//                                eachShuttle.add(price + " TL");
//                            }
//                        }


//                        returningScheduleList.add(eachShuttle);
//
//                        Log.d("database deneme", " " + returningScheduleList.toString());
//                        String eachShuttleTime = eachShuttle.get(2);
//                        String[] eachShuttleHourAndMinute = eachShuttleTime.split(":");
//                        int eachShuttleHour = Integer.parseInt(eachShuttleHourAndMinute[0]);
//                        int eachShuttleMinute = Integer.parseInt(eachShuttleHourAndMinute[1]);
//
//                        int eachShuttleTotal = eachShuttleHour*60 + eachShuttleMinute;
//                        Log.d("saat deneme"," " +eachShuttleTotal);
//                        Log.d("saat deneme"," " + currentTimeTotal);
//                        //if(eachShuttleTotal > currentTimeTotal) {
//
//                            scrollViewLinearLayout.addView(createNewTextView(eachShuttle.get(0),eachShuttle.get(1),eachShuttle.get(2),eachShuttle.get(3)));
//                            //leftLinearLayout.addView(createNewTextView(eachShuttle.get(0)));
//                            //rightLinearLayout.addView(createNewTextView(eachShuttle.get(3)));
//                            //centerLeftLinearLayout.addView(createNewTextView(eachShuttle.get(1)));
//                            //centerRightLinearLayout.addView(createNewTextView(eachShuttle.get(2)));
//
//                        //}
//                        //String availability;
//                        Log.d("deneme", eachShuttle.toString());
//                        //Log.d("deneme", availability);
                    }

                    */
                }

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



                Schedule_From_Spinnerr.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("failDeneme","u failed bitch");
            }
        });


        Schedule_From_Spinnerr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = Schedule_From_Spinnerr.getSelectedItem().toString();
                //Log.wtf("TEXT DEGISTIII",text);
                //Log.wtf("TEXT DEGISTIII",fromToTable.get(text).toString());
                //Log.wtf("TEXT DEGISTIII========="+fromToTable.get(text).size(),"hohoho");
                //Log.wtf("asdfasdf",fromToTable.get(text).toString());

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, fromToTable.get(text));

                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter2.notifyDataSetChanged();
                Schedule_To_Spinnerr.setAdapter(adapter2);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<ArrayList<String>> myList = readFromDatabase();
                /*
                scrollViewLinearLayout.removeAllViews();

                String toSpinnerValue = Schedule_To_Spinnerr.getSelectedItem().toString();
                String fromSpinnerValue = Schedule_From_Spinnerr.getSelectedItem().toString();
                ArrayList<ArrayList<String>> myList = readFromDatabase(fromSpinnerValue,toSpinnerValue);

                for(ArrayList<String> eachShuttle : myList){

                    scrollViewLinearLayout.addView(createNewTextView(fromSpinnerValue,toSpinnerValue,eachShuttle.get(0),eachShuttle.get(2)));
                }

                */
            }
        });



        //ArrayList<ArrayList<String>> myList = readFromDatabase();
        //myList = readFromDatabase();
        //Log.d("database deneme", " " + returningScheduleList.toString());

        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
            }
        }, 10000);
        */
        /*
        for(ArrayList<String> eachShuttle : returningScheduleList){
            String eachShuttleTime = eachShuttle.get(2);
            String[] eachShuttleHourAndMinute = eachShuttleTime.split(":");
            int eachShuttleHour = Integer.parseInt(eachShuttleHourAndMinute[0]);
            int eachShuttleMinute = Integer.parseInt(eachShuttleHourAndMinute[1]);

            int eachShuttleTotal = eachShuttleHour*60 + eachShuttleMinute;
            Log.d("saat deneme"," " +eachShuttleTotal);
            Log.d("saat deneme"," " + currentTimeTotal);
            if(eachShuttleTotal > currentTimeTotal) {

                leftLinearLayout.addView(createNewTextView(eachShuttle.get(0)));
                rightLinearLayout.addView(createNewTextView(eachShuttle.get(3)));
                centerLeftLinearLayout.addView(createNewTextView(eachShuttle.get(1)));
                centerRightLinearLayout.addView(createNewTextView(eachShuttle.get(2)));

            }

        }
        */
        return v;
    }

    public TextView createNewTextView(String fromText, String toText, String timeText, String priceText) {

        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(getActivity());
        textView.setLayoutParams(lparams);
        textView.setText(translateToWantedString(fromText,20) + translateToWantedString(toText,15) + translateToWantedString(timeText,10) + translateToWantedString(priceText,10));
        return textView;
    }

    public String translateToWantedString(String text,int wantedLength) {
        int textLength = text.length();
        if(textLength < wantedLength) {
            for(int i = 0; i<wantedLength - textLength; i++) {
                text += " ";
            }
        }
        return text;
    }


    public ArrayList<ArrayList<String>> readFromDatabase() {
        //ArrayList<ArrayList<String>> returningList = new ArrayList<ArrayList<String>>();


        scrollViewLinearLayout.removeAllViews();

        final String toSpinnerValue = Schedule_To_Spinnerr.getSelectedItem().toString();
        final String fromSpinnerValue = Schedule_From_Spinnerr.getSelectedItem().toString();




        myRef.child("Routes").child(fromSpinnerValue+"-"+toSpinnerValue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();
                returningList = new ArrayList<ArrayList<String>>();

                route = dataSnapshot.getValue(Route.class);
                ArrayList<String> timeList = new ArrayList<>();
                if(isWeekday){

                    timeList.addAll(route.getWeekdayTimes().keySet());
                    Collections.sort(timeList);
                    for(String eachtime : timeList) {
                        ArrayList<String> eachShuttle = new ArrayList<String>();
                        eachShuttle.add(eachtime);
                        int availability = route.getWeekdayTimesValue(eachtime).availability;
                        eachShuttle.add(String.valueOf(availability));
                        double price = route.getWeekdayTimesValue(eachtime).getPrice();
                        eachShuttle.add(String.valueOf(price)+ " TL");

                        if(eachShuttle.get(1).equals("0")){
                            eachShuttle.add("No Button");
                        }
                        else {
                            eachShuttle.add("Button");
                        }
                        eachShuttle.add("weekdayTimes");
                        returningList.add(eachShuttle);
                        myList.add(eachShuttle);

                    }
                }
                else {
                    timeList.addAll(route.getWeekendTimes().keySet());
                    Collections.sort(timeList);

                    for(String eachtime : timeList) {
                        ArrayList<String> eachShuttle = new ArrayList<String>();
                        eachShuttle.add(eachtime);
                        int availability = route.getWeekdayTimesValue(eachtime).availability;
                        eachShuttle.add(String.valueOf(availability));
                        double price = route.getWeekdayTimesValue(eachtime).getPrice();
                        eachShuttle.add(String.valueOf(price)+ " TL");

                        if(eachShuttle.get(1).equals("0")){
                            eachShuttle.add("No Button");
                        }
                        else {
                            eachShuttle.add("Button");
                        }
                        eachShuttle.add("weekendTimes");
                        returningList.add(eachShuttle);
                        myList.add(eachShuttle);
                    }
                }

                for(ArrayList<String> eachShuttle : myList){

                    scrollViewLinearLayout.addView(createNewTextView(fromSpinnerValue,toSpinnerValue,eachShuttle.get(0),eachShuttle.get(2)));
                }

                Log.d("route deneme",route.toString());


                /*
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> eachShuttle = new ArrayList<String>();

                    String time = singleSnapshot.getKey().toString();
                    eachShuttle.add(time);

                    String availability = "";
                    String price = "";
                    for(DataSnapshot eachSnapshot :singleSnapshot.getChildren()){
                        if(eachSnapshot.getKey().equals("Availability")){
                            availability = eachSnapshot.getValue().toString();
                            eachShuttle.add(availability);
                        }

                        if(eachSnapshot.getKey().equals("Price")){
                            price = eachSnapshot.getValue().toString();
                            eachShuttle.add(price+" TL");
                        }
                    }

                    if(eachShuttle.get(1).equals("0")){
                        eachShuttle.add("No Button");
                    }
                    else {
                        eachShuttle.add("Button");
                    }

                    returningList.add(eachShuttle);
                    //String availability;
                    //Log.d("deneme", eachShuttle.toString());
                    //Log.d("deneme", availability);
                }
*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.d("failDeneme","u failed bitch");
            }
        });


        return returningList;
    }

    /*
    public ArrayList<ArrayList<String>> readFromDatabase() {
        //ArrayList<ArrayList<String>> returningList = new ArrayList<ArrayList<String>>();

        myRef.child("Routes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                returningScheduleList = new ArrayList<ArrayList<String>>();

                for (DataSnapshot eachFromToSnapshot : dataSnapshot.getChildren()) {

                    String fromTo = eachFromToSnapshot.getKey().toString();
                    String[] fromToParsed = fromTo.split("-");
                    String from = fromToParsed[0];
                    String to = fromToParsed[1];


                    for (DataSnapshot singleSnapshot : eachFromToSnapshot.getChildren()) {
                        ArrayList<String> eachShuttle = new ArrayList<String>();

                        eachShuttle.add(from);
                        eachShuttle.add(to);

                        String time = singleSnapshot.getKey().toString();
                        eachShuttle.add(time);

                        String availability = "";
                        String price = "";
                        for (DataSnapshot eachSnapshot : singleSnapshot.getChildren()) {

                            if (eachSnapshot.getKey().equals("Price")) {
                                price = eachSnapshot.getValue().toString();
                                eachShuttle.add(price + " TL");
                            }
                        }


                        returningScheduleList.add(eachShuttle);
                        Log.d("database deneme", " " + returningScheduleList.toString());

                        //String availability;
                        Log.d("deneme", eachShuttle.toString());
                        //Log.d("deneme", availability);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("failDeneme","u failed bitch");
            }
        });

        Log.d("database deneme", " " + returningScheduleList.toString());
        return returningScheduleList;
    }

    */

    public void doWork() {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView txtCurrentTime= (TextView) mainView.findViewById(R.id.Schedule_Text_View);
                    Date dt = new Date();
                    int hours = dt.getHours();
                    //currentTimeHour = hours;
                    //Log.d("deneme", "hey " + hours);
                    //Log.d("deneme", "hey " + currentTimeHour);
                    int minutes = dt.getMinutes();
                    //currentTimeMinute = minutes;
                    int seconds = dt.getSeconds();
                    String curTime = hours + ":" + minutes + ":" + seconds;
                    txtCurrentTime.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }
    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

}

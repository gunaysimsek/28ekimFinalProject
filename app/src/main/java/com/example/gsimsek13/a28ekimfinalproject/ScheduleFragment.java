package com.example.gsimsek13.a28ekimfinalproject;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Date;

/**
 * Created by Bartu on 11/13/2017.
 */

public class ScheduleFragment extends Fragment{

    TextView CurrentTime;
    View mainView;
    int currentTimeHour;
    int currentTimeMinute;
    int currentTimeTotal;
    ArrayList<ArrayList<String>> returningScheduleList = new ArrayList<ArrayList<String>>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    LinearLayout leftLinearLayout;
    LinearLayout rightLinearLayout;
    LinearLayout centerLeftLinearLayout;
    LinearLayout centerRightLinearLayout;
    LinearLayout scrollViewLinearLayout;
    ScrollView scheduleScrollView;

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

        //String time = CurrentTime.getText().toString();
        //String[] seperatedTime = time.split(":");
        currentTimeTotal = currentTimeHour*60 + currentTimeMinute;
        //Log.d("deneme", "hey " + currentTimeTotal);
        //CurrentTime.setText(currentTime.toString());


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
                        String eachShuttleTime = eachShuttle.get(2);
                        String[] eachShuttleHourAndMinute = eachShuttleTime.split(":");
                        int eachShuttleHour = Integer.parseInt(eachShuttleHourAndMinute[0]);
                        int eachShuttleMinute = Integer.parseInt(eachShuttleHourAndMinute[1]);

                        int eachShuttleTotal = eachShuttleHour*60 + eachShuttleMinute;
                        Log.d("saat deneme"," " +eachShuttleTotal);
                        Log.d("saat deneme"," " + currentTimeTotal);
                        //if(eachShuttleTotal > currentTimeTotal) {

                            scrollViewLinearLayout.addView(createNewTextView(eachShuttle.get(0),eachShuttle.get(1),eachShuttle.get(2),eachShuttle.get(3)));
                            //leftLinearLayout.addView(createNewTextView(eachShuttle.get(0)));
                            //rightLinearLayout.addView(createNewTextView(eachShuttle.get(3)));
                            //centerLeftLinearLayout.addView(createNewTextView(eachShuttle.get(1)));
                            //centerRightLinearLayout.addView(createNewTextView(eachShuttle.get(2)));

                        //}
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


        //ArrayList<ArrayList<String>> myList = readFromDatabase();
        //myList = readFromDatabase();
        Log.d("database deneme", " " + returningScheduleList.toString());

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

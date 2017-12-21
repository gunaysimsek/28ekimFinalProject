package com.example.gsimsek13.a28ekimfinalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Bartu on 11/30/2017.
 */

public class MyReservationsFragment extends Fragment {


    String userName;
    int currentTimeHour;
    int currentTimeMinute;
    int currentTimeTotal;
    Boolean isWeekday;
    String weekDayOrWeekend;
    Calendar calender;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    List<Reservations> userReservations;
    RadioGroup myRadioGroup;
    Button rateDriver;
    LinearLayout ScrollViewLayout;
    int rating;
    int raters;


    public MyReservationsFragment() {

    }

    @SuppressLint("ValidFragment")
    public MyReservationsFragment(String name) {
        userName = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getActivity().getActionBar().setTitle("My Reservations");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Reservations");

        View v = inflater.inflate(R.layout.fragment_my_reservations, container, false);
        if (savedInstanceState != null) {
            userName = savedInstanceState.getString("userName");
        }


        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if ((day != 7 && day != 1)) {
            isWeekday = true;
            weekDayOrWeekend = "weekdayTimes";
        } else {
            isWeekday = false;
            weekDayOrWeekend = "weekendTimes";
        }


        ScrollViewLayout = v.findViewById(R.id.my_reservations_linear_layout);

        myRef.child("Customers").child(userName).child("reservations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userReservations = new ArrayList<Reservations>();
                for (DataSnapshot eachSnapshot : dataSnapshot.getChildren()) {
                    //Log.d("MyReservations deneme", userName);
                    //Log.d("MyReservations deneme",eachSnapshot.toString());
                    Reservations eachRouteReservation = eachSnapshot.getValue(Reservations.class);

                    //Log.d("MyReservations deneme",eachRouteReservation.toString());

                    userReservations.add(eachRouteReservation);
                }


                for (Reservations eachReservation : userReservations) {
                    final String from = eachReservation.from;
                    final String to = eachReservation.to;

                    //Log.d("My Reservations deneme","From: " +eachReservation.from + " To: " +eachReservation.to);
                    for (String eachTime : eachReservation.getTimes().keySet()) {
                        final String time = eachTime;
                        String theDay;
                        final String theTimeList;
                        final String keyToValue = eachReservation.getTimesValue(time);
                        //final String driverName = "";
                        //final Times shuttleTimeClass;
                        if (isWeekday) {
                            theDay = "weekdayTimes";
                            theTimeList = "weekdayTimeList";
                        } else {
                            theDay = "weekendTimes";
                            theTimeList = "weekendTimeList";
                        }
                        //Log.d("Timesdeneme",from+"-"+to +" "+theDay+ " " +time);
                        myRef.child("Routes").child(from + "-" + to).child(theDay).child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Times shuttleTimeClass = dataSnapshot.getValue(Times.class);
                                //Log.d("Timesdeneme",shuttleTimeClass.toString());
                                final double price = shuttleTimeClass.getPrice();
                                final String driverName = shuttleTimeClass.driver;
                                Log.d("Timesdeneme", driverName + "  " + time);
                                //ScrollViewLayout.addView(createNewTextView("From: " +from + " To: " + to + " Time: " + time));


                                myRef.child("Drivers").child(driverName).child("routes").child(from + "-" + to).child(theTimeList).child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.d("Timessss", driverName + " " + from + "-" + to + " " + theTimeList + " " + time);
                                        Log.d("Timesdeneme", dataSnapshot.getValue().toString());
                                        String value = dataSnapshot.getValue(String.class);
                                        Log.d("Timesdeneme", value);
                                        ScrollViewLayout.addView(createNewTextView("From: " + from + " To: " + to + " Time: " + time));

                                        if (value.equalsIgnoreCase("Ended") && keyToValue.equalsIgnoreCase("unrated")) {
                                            ScrollViewLayout.addView(createNewButton(driverName, true,from+"-"+to,time));
                                        }
                                        else if((value.equalsIgnoreCase("Active") || value.equalsIgnoreCase("Standby")) && keyToValue.equalsIgnoreCase("unrated")){
                                            ScrollViewLayout.addView(createCancellationButton(driverName,from+"-"+to,time,price));
                                        }
                                        else {
                                            ScrollViewLayout.addView(createNewButton(driverName, false,from+"-"+to,time));
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        //Log.d("My Reservations deneme", eachTime);
                    }

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }


    public TextView createNewTextView(String text) {

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        final TextView textView = new TextView(getActivity());
        lparams.setMargins(0,getResources().getInteger(R.integer.buttonMargin),0,0);

        textView.setLayoutParams(lparams);
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        //textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.textSize));
        textView.setText(text);
        //textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        textView.setTextColor(Color.BLACK);

        //textView.setBackgroundResource(R.drawable.border_color_light_blue);
        //textView.setTextColor(Color.BLACK);

        //textView.setBackgroundColor(Color.RED);
        return textView;
    }

    public Button createCancellationButton(final String driver, final String fromto, final String time,final double price) {

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Button newButton = new Button(getActivity());
        newButton.setText("Cancel Reservation");
        newButton.setTextColor(Color.WHITE);
        newButton.setLayoutParams(lparams);

        Date dt = new Date();
        int hours = dt.getHours();
        currentTimeHour = hours;
        //Log.d("deneme", "hey " + hours);
        //Log.d("deneme", "hey " + currentTimeHour);
        int minutes = dt.getMinutes();
        currentTimeMinute = minutes;

        currentTimeTotal = currentTimeHour*60 + currentTimeMinute;


        String[] eachReservationHourAndMinute = time.split(":");
        int eachReservationHour = Integer.parseInt(eachReservationHourAndMinute[0]);
        int eachReservationMinute = Integer.parseInt(eachReservationHourAndMinute[1]);
        int eachReservationTotal = eachReservationHour*60 + eachReservationMinute;

        if(currentTimeTotal >= eachReservationTotal){
            newButton.setVisibility(View.INVISIBLE);
            newButton.setClickable(false);
        }
        else {
            newButton.setVisibility(View.VISIBLE);
        }

        newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancelreservationicon,0,0,0);

        newButton.setHeight(getResources().getInteger(R.integer.buttonHeigth));
        //newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.makereservationicon,0,0,0);
        //newButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        newButton.setBackgroundResource(R.drawable.border_color_dark_red);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Customers").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double currentBalance = dataSnapshot.child("balance").getValue(double.class);
                        myRef.child("Customers").child(userName).child("balance").setValue(currentBalance + price);
                        myRef.child("Customers").child(userName).child("reservations").child(fromto).child("times").child(time).removeValue();
                        myRef.child("Routes").child(fromto).child(weekDayOrWeekend).child(time).child("users").child(userName).removeValue();


                        myRef.child("Routes").child(fromto).child(weekDayOrWeekend).child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Times timesClass = dataSnapshot.getValue(Times.class);
                                int availability = timesClass.getAvailability();

                                myRef.child("Routes").child(fromto).child(weekDayOrWeekend).child(time).child("availability").setValue(availability + 1);
                                newButton.setVisibility(View.INVISIBLE);
                                newButton.setClickable(false);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return newButton;
    }
    public Button createNewButton(final String driver, final boolean visible,final String fromTo,final String time) {
        //Log.wtf("Dallama",fromTo);
        //Log.wtf("Dallama",time);
        //Log.wtf("Dallama",user);
        //Log.wtf("Dallama",price);

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final Button newButton = new Button(getActivity());
        newButton.setText("Rate Driver");
        newButton.setTextColor(Color.WHITE);
        newButton.setLayoutParams(lparams);


        newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ratedriver,0,0,0);

        newButton.setHeight(getResources().getInteger(R.integer.buttonHeigth));
        //newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.makereservationicon,0,0,0);
        //newButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        newButton.setBackgroundResource(R.drawable.border_color_light_blue);


        //newButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if (visible) {
            newButton.setVisibility(View.VISIBLE);
        } else {
            newButton.setVisibility(View.INVISIBLE);
            newButton.setClickable(false);
        }
        newButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                                             View mView = getLayoutInflater().inflate(R.layout.dialog_rate_driver, null);
                                             myRadioGroup = (RadioGroup) mView.findViewById(R.id.Rate_Driver_Radio_Group);
                                             rateDriver = (Button) mView.findViewById(R.id.rate_Driver_Button);

                                             mBuilder.setView(mView);
                                             final AlertDialog dialog = mBuilder.create();
                                             dialog.show();

                                             rateDriver.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     int radioButtonID = myRadioGroup.getCheckedRadioButtonId();

                                                     RadioButton radioButton = (RadioButton) myRadioGroup.findViewById(radioButtonID);
                                                     int radioId = myRadioGroup.indexOfChild(radioButton);
                                                     RadioButton btn = (RadioButton) myRadioGroup.getChildAt(radioId);
                                                     String selection = (String) btn.getText();
                                                     Log.d("RadioButtonDeneme", radioButtonID + "");
                                                     int driverRating;

                                                     if (selection.equalsIgnoreCase("Very Bad")) {
                                                         driverRating = 1;
                                                     } else if (selection.equalsIgnoreCase("Bad")) {
                                                         driverRating = 2;
                                                     } else if (selection.equalsIgnoreCase("Average")) {
                                                         driverRating = 3;
                                                     } else if (selection.equalsIgnoreCase("Good")) {
                                                         driverRating = 4;
                                                     } else {
                                                         driverRating = 5;
                                                     }
                                                     final int finalDriverRating = driverRating;
                                                     Log.d("FailDeneme", "hooooop");

                                                     myRef.child("Drivers").child(driver).addListenerForSingleValueEvent(new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(DataSnapshot dataSnapshot) {


                                                             rating = dataSnapshot.child("rating").getValue(int.class);
                                                             raters = dataSnapshot.child("raters").getValue(int.class);
                                                             myRef.child("Drivers").child(driver).child("rating").setValue(rating + finalDriverRating);
                                                             myRef.child("Drivers").child(driver).child("raters").setValue(raters + 1);
                                                             myRef.child("Customers").child(userName).child("reservations").child(fromTo).child("times").child(time).setValue("rated");

                                                             dialog.hide();
                                                             newButton.setVisibility(View.INVISIBLE);
                                                             newButton.setClickable(false);

                                                         }

                                                         @Override
                                                         public void onCancelled(DatabaseError databaseError) {

                                                         }
                                                     });


                                                     //Log.d("RadioButtonDeneme",selection+"hoop");
                                                 }
                                             });

                                         }
                                     }

        );
        return newButton;


    }

    /*
    public String findDriver(String from,String to,String time,boolean isWeekdayy){
        String theDay;
        final String driverName = "";
        //final Times shuttleTimeClass;
        if(isWeekdayy){
            theDay = "weekDayTimeList";
        }
        else {
            theDay = "weekendTimeList";
        }
        myRef.child(from+"-"+to).child(theDay).child(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Times shuttleTimeClass = dataSnapshot.getValue(Times.class);
                String driverName = shuttleTimeClass.driver;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return driverName;
    }
    */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("userName", userName);
        // etc.
    }

}

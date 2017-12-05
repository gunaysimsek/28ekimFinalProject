package com.example.gsimsek13.a28ekimfinalproject;

import android.app.AlertDialog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Bartu on 11/8/2017.
 */

// Efosun kodunda bug var. Her make reservation buttonuna bastiginda spinnerlar eski haline donuyor.
public class ReservationFragment extends Fragment {

    //TextView exampleTV;
    boolean alreadyRegistered = false;
    Spinner toSpin;
    Spinner fromSpin;
    Button from_to_button;
    GridLayout make_Reservation_GridLayout;
    LinearLayout leftLinearLayout;
    LinearLayout rightLinearLayout;
    LinearLayout centerLeftLinearLayout;
    LinearLayout centerRightLinearLayout;
    //ArrayList<ArrayList<String>> returningList2 = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> returningList = new ArrayList<ArrayList<String>>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private String[] parts;
    double userBalance;
    Calendar calender;
    boolean isWeekday = true;
    HashMap<String,ArrayList<String>> fromToTable = new HashMap<>();
    Route route;
    int routeAvailability;









    public ReservationFragment() {

    }

    public ArrayList<ArrayList<String>> readFromDatabase() {
        //ArrayList<ArrayList<String>> returningList = new ArrayList<ArrayList<String>>();

        leftLinearLayout.removeAllViews();
        centerRightLinearLayout.removeAllViews();
        centerLeftLinearLayout.removeAllViews();
        rightLinearLayout.removeAllViews();



        //exampleTV.setText(toSpinner.getSelectedItem().toString());
        //exampleTV.setVisibility(View.VISIBLE);

        final String toSpinnerValue = toSpin.getSelectedItem().toString();
        final String fromSpinnerValue = fromSpin.getSelectedItem().toString();

        myRef.child("Routes").child(fromSpinnerValue+"-"+toSpinnerValue).addValueEventListener(new ValueEventListener() {
            @Override



            public void onDataChange(DataSnapshot dataSnapshot) {
                returningList = new ArrayList<ArrayList<String>>();
                ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();
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

                Log.d("route deneme",route.toString());

                for(ArrayList<String> eachShuttle : myList){
                    //Log.d("deneme",eachShuttle.get(1));
                    //Log.d("deneme",eachShuttle.get(3));
                    //Log.d("deneme",eachShuttle.get(0));
                    //Log.d("deneme",eachShuttle.get(2));

                    leftLinearLayout.addView(createNewTextView(eachShuttle.get(1)));
                    if(eachShuttle.get(3).equalsIgnoreCase("Button")) {
                        rightLinearLayout.addView(createNewButton(fromSpinnerValue+"-"+toSpinnerValue,eachShuttle.get(0),parts[0],true,eachShuttle.get(2),eachShuttle.get(4),Integer.parseInt(eachShuttle.get(1))));
                    }
                    else {
                        rightLinearLayout.addView(createNewButton(fromSpinnerValue+"-"+toSpinnerValue,eachShuttle.get(0),parts[0],false,eachShuttle.get(2),eachShuttle.get(4),Integer.parseInt(eachShuttle.get(1))));
                    }
                    //rightLinearLayout.addView(createNewTextView(eachShuttle.get(3)));
                    centerLeftLinearLayout.addView(createNewTextView(eachShuttle.get(0)));
                    centerRightLinearLayout.addView(createNewTextView(eachShuttle.get(2)));

                }
                if(leftLinearLayout.getChildCount() > 0) {
                    //Log.d("deneme", "Left child sayisi " + leftLinearLayout.getChildCount());
                    for(int i=0; i<leftLinearLayout.getChildCount() ; i++){
                        //Log.d("deneme", "Left child "+i+" Idsi " + leftLinearLayout.getChildAt(i).get(i));

                    }
                }
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Log.d("deneme","hopkiri");

        View v = inflater.inflate(R.layout.fragment_reservation,container,false );

        from_to_button = (Button) v.findViewById(R.id.from_to_button);
        //exampleTV = (TextView) v.findViewById(R.id.exampleTextView);
        make_Reservation_GridLayout = (GridLayout) v.findViewById(R.id.Make_Reservation_GridLayout);
        leftLinearLayout = (LinearLayout) v.findViewById(R.id.leftLinearLayout);
        rightLinearLayout = (LinearLayout) v.findViewById(R.id.rightLinearLayout) ;
        centerLeftLinearLayout = (LinearLayout) v.findViewById(R.id.centerLeftLinearLayout);
        centerRightLinearLayout = (LinearLayout) v.findViewById(R.id.centerRightLinearLayout);

        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");

        toSpin = (Spinner) v.findViewById(R.id.toSpinner);
        fromSpin = (Spinner) v.findViewById(R.id.fromSpinner);

        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if((day!=7 && day !=1)){
            isWeekday = true;
        }else{
            isWeekday = false;
        }


        myRef.child("Routes").addValueEventListener(new ValueEventListener() { //BURASI DEGISICEK
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

/*
        rateDriverFragmentOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_rate_driver,null);
                myRadioGroup = (RadioGroup) mView.findViewById(R.id.Rate_Driver_Radio_Group);
                rateDriver = (Button) mView.findViewById(R.id.rate_Driver_Button) ;

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                rateDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int radioButtonID = myRadioGroup.getCheckedRadioButtonId();

                        RadioButton radioButton = (RadioButton) myRadioGroup.findViewById(radioButtonID);
                        Log.d("RadioButtonDeneme",radioButtonID +"");
                    }
                });

            }
        });
*/
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



        //Log.d("Reservation deneme",parts[0]);
        from_to_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

//                leftLinearLayout.removeAllViews();
//                centerRightLinearLayout.removeAllViews();
//                centerLeftLinearLayout.removeAllViews();
//                rightLinearLayout.removeAllViews();
//
//
//
//
//
//                String toSpinnerValue = toSpin.getSelectedItem().toString();
//                String fromSpinnerValue = fromSpin.getSelectedItem().toString();





                ArrayList<ArrayList<String>> myList = readFromDatabase();



                //Log.d("deneme",myList.toString());

                //leftLinearLayout.addView(createNewTextView(myList.get(0).get(1)));
                //rightLinearLayout.addView(createNewTextView(myList.get(0).get(3)));
                //centerLeftLinearLayout.addView(createNewTextView(myList.get(0).get(0)));
                //centerRightLinearLayout.addView(createNewTextView(myList.get(0).get(2)));

//                for(ArrayList<String> eachShuttle : myList){
//
//
//                    leftLinearLayout.addView(createNewTextView(eachShuttle.get(1)));
//                    if(eachShuttle.get(3).equalsIgnoreCase("Button")) {
//                        rightLinearLayout.addView(createNewButton(fromSpinnerValue+"-"+toSpinnerValue,eachShuttle.get(0),parts[0],true,eachShuttle.get(2),eachShuttle.get(4)));
//                    }
//                    else {
//                        rightLinearLayout.addView(createNewButton(fromSpinnerValue+"-"+toSpinnerValue,eachShuttle.get(0),parts[0],false,eachShuttle.get(2),eachShuttle.get(4)));
//                    }
//                    centerLeftLinearLayout.addView(createNewTextView(eachShuttle.get(0)));
//                    centerRightLinearLayout.addView(createNewTextView(eachShuttle.get(2)));
//
//                }
//                if(leftLinearLayout.getChildCount() > 0) {
//                    //Log.d("deneme", "Left child sayisi " + leftLinearLayout.getChildCount());
//                    for(int i=0; i<leftLinearLayout.getChildCount() ; i++){
//                        //Log.d("deneme", "Left child "+i+" Idsi " + leftLinearLayout.getChildAt(i).get(i));
//
//                    }
//                }
            }
        });

        //toSpinner.
        //exampleTV = (TextView) v.findViewById(R.id.exampleTextView);
        //exampleTV.setText(toSpinnerValue);
        /*
        nametv = (TextView) v.findViewById(R.id.profileNameTxt);



        User user = new User();
        user.name = "Gunay";
        user.surname = "Simsek";
        user.email = "gunayberkaysimsek@gmail.com";
        user.phoneNumber = 538392;




        nametv.setText("Bartu");
        */

        return v;
    }

    public void bringUpData(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState, View v) {
        from_to_button = (Button) v.findViewById(R.id.from_to_button);
        toSpin = (Spinner) v.findViewById(R.id.toSpinner);
        //exampleTV = (TextView) v.findViewById(R.id.exampleTextView);

    }

    public TextView createNewTextView(String text) {

        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(300, 100);
        final TextView textView = new TextView(getActivity());

        textView.setLayoutParams(lparams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(text);
        return textView;
    }

    public Button createNewButton(final String fromTo,final String time,final String user, boolean visible,final String price,final String weekDay,final int availability) {
        //Log.wtf("Dallama",fromTo);
        //Log.wtf("Dallama",time);
        //Log.wtf("Dallama",user);
        //Log.wtf("Dallama",price);

        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(300,100);
        final Button newButton = new Button(getActivity());
        newButton.setLayoutParams(lparams);
        if(visible) {
            newButton.setVisibility(View.VISIBLE);
        }
        else{
            newButton.setVisibility(View.INVISIBLE);
            newButton.setClickable(false);
        }
        newButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Log.d("Reservation deneme", "hoooop");
                                             WriteUserToTheRoute(fromTo,time,user,price,weekDay,availability);
                                         }
                                     }

        );
        return newButton;


    }
    public void WriteUserToTheRoute(final String fromTo, final String time, final String user, final String price, final String currentDay,final int availability) {
        //String userBalance;


        Log.d("Reservation deneme","bak burdayim");

        myRef.child("Customers").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Customer wantedCustomer = dataSnapshot.getValue(Customer.class);
                userBalance = wantedCustomer.balance;



                if( (int) userBalance >= Integer.parseInt(price.substring(0,2))){
                    myRef.child("Routes").child(fromTo).child(currentDay).child(time).child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Log.d("singleSnapshotlii", singleSnapshot.getKey().toString() + "   " + user);
                                if (singleSnapshot.getKey().toString().equalsIgnoreCase(user)) {
                                    alreadyRegistered = true;


                                }

                            }

                            if (alreadyRegistered) {
                                Toast.makeText(getContext(), "You already made reservation for that shuttle", Toast.LENGTH_SHORT).show();
                            } else {
                                myRef.child("Routes").child(fromTo).child(currentDay).child(time).child("users").child(user).setValue(user);
                                myRef.child("Customers").child(parts[0]).child("reservations").child(fromTo).child("times").child(time).setValue(time);
                                myRef.child("Routes").child(fromTo).child(currentDay).child(time).child("availability").setValue(availability - 1);
                                //int availability = myRef.child("Routes").child(fromTo).child(currentDay).child(time).child("availability");
                                //myRef.child("Routes").child(fromTo).child(currentDay).child(time).child("availability").setValue()
                                myRef.child("Customers").child(user).child("balance").setValue(userBalance - Integer.parseInt(price.substring(0, 2)));
                            }

                            alreadyRegistered = false;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


                else {
                    Log.d("Reservation Deneme", "User in parasi = " +userBalance +", Gereken para = "+ price.substring(0,2));
                }
                //userBalance = Double.toString(wantedCustomer.balance);
                /*
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.getKey().toString().equalsIgnoreCase(parts[0])) {
                        Log.d("Dallama","hoooop");
                        String snapshot = singleSnapshot.getKey().toString();
                        Log.d("rezervasyon", snapshot);
                        if (snapshot.equalsIgnoreCase("balance")) {
                            Log.d("Dallama", snapshot);
                            userBalance = singleSnapshot.getValue().toString();

                        }
                    }
                }
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



}


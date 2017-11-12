package com.example.gsimsek13.a28ekimfinalproject;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Bartu on 11/8/2017.
 */

public class ReservationFragment extends Fragment {

    //TextView exampleTV;
    Spinner toSpinner;
    Spinner fromSpinner;
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






    public ReservationFragment() {

    }

    public ArrayList<ArrayList<String>> readFromDatabase(String from, String to) {
        //ArrayList<ArrayList<String>> returningList = new ArrayList<ArrayList<String>>();

        myRef.child("Routes").child(from+"-"+to).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                returningList = new ArrayList<ArrayList<String>>();


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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("failDeneme","u failed bitch");
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
        toSpinner = (Spinner) v.findViewById(R.id.toSpinner);
        fromSpinner = (Spinner) v.findViewById(R.id.fromSpinner);
        //exampleTV = (TextView) v.findViewById(R.id.exampleTextView);
        make_Reservation_GridLayout = (GridLayout) v.findViewById(R.id.Make_Reservation_GridLayout);
        leftLinearLayout = (LinearLayout) v.findViewById(R.id.leftLinearLayout);
        rightLinearLayout = (LinearLayout) v.findViewById(R.id.rightLinearLayout) ;
        centerLeftLinearLayout = (LinearLayout) v.findViewById(R.id.centerLeftLinearLayout);
        centerRightLinearLayout = (LinearLayout) v.findViewById(R.id.centerRightLinearLayout);


        from_to_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                leftLinearLayout.removeAllViews();
                centerRightLinearLayout.removeAllViews();
                centerLeftLinearLayout.removeAllViews();
                rightLinearLayout.removeAllViews();



                //exampleTV.setText(toSpinner.getSelectedItem().toString());
                //exampleTV.setVisibility(View.VISIBLE);

                String toSpinnerValue = toSpinner.getSelectedItem().toString();
                String fromSpinnerValue = fromSpinner.getSelectedItem().toString();
                ArrayList<ArrayList<String>> myList = readFromDatabase(fromSpinnerValue,toSpinnerValue);
                Log.d("deneme",myList.toString());

                //leftLinearLayout.addView(createNewTextView(myList.get(0).get(1)));
                //rightLinearLayout.addView(createNewTextView(myList.get(0).get(3)));
                //centerLeftLinearLayout.addView(createNewTextView(myList.get(0).get(0)));
                //centerRightLinearLayout.addView(createNewTextView(myList.get(0).get(2)));


                for(ArrayList<String> eachShuttle : myList){
                    Log.d("deneme",eachShuttle.get(1));
                    Log.d("deneme",eachShuttle.get(3));
                    Log.d("deneme",eachShuttle.get(0));
                    Log.d("deneme",eachShuttle.get(2));

                    leftLinearLayout.addView(createNewTextView(eachShuttle.get(1)));
                    rightLinearLayout.addView(createNewTextView(eachShuttle.get(3)));
                    centerLeftLinearLayout.addView(createNewTextView(eachShuttle.get(0)));
                    centerRightLinearLayout.addView(createNewTextView(eachShuttle.get(2)));

                }
                if(leftLinearLayout.getChildCount() > 0) {
                    Log.d("deneme", "Left child sayisi " + leftLinearLayout.getChildCount());
                    for(int i=0; i<leftLinearLayout.getChildCount() ; i++){
                        //Log.d("deneme", "Left child "+i+" Idsi " + leftLinearLayout.getChildAt(i).get(i));

                    }
                }
                //leftLinearLayout.addView(createNewTextView("3"));
                //rightLinearLayout.addView(createNewTextView("Button"));
                //centerLeftLinearLayout.addView(createNewTextView("10:30"));
                //centerRightLinearLayout.addView(createNewTextView("15 TL"));


                //Log.d("deneme",fromSpinnerValue+"-"+toSpinnerValue);
                //readFromDatabase(fromSpinnerValue,toSpinnerValue);
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
        toSpinner = (Spinner) v.findViewById(R.id.toSpinner);
        //exampleTV = (TextView) v.findViewById(R.id.exampleTextView);

    }

    public TextView createNewTextView(String text) {

        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(getActivity());
        textView.setLayoutParams(lparams);
        textView.setText(text);
        return textView;
    }

}

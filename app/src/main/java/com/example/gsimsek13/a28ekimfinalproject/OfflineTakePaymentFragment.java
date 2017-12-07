package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
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
public class OfflineTakePaymentFragment extends Fragment {

    Button takePaymentBtn;
    private Spinner toSpin;
    private Spinner fromSpin;
    private Spinner timeSpin;

    private FirebaseDatabase takeDatabase;
    private DatabaseReference takeRef;

    private boolean isWeekday = true;
    private HashMap<String, ArrayList<String>> fromToTable = new HashMap<>();
    private Route route;
    private Calendar calender;

    private long takePrice;
    private long takeAvailability;
    private int takeNewAvailability;

    private long takeBalance;
    private long takeNewBalance;


    private EditText offlineTakeEmailET;
    private EditText offlineTakePasswordET;


    private String driverID;
    private String driverEmail;
    private String driverPassword;

    public OfflineTakePaymentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Password Payment");
        takeDatabase = FirebaseDatabase.getInstance();
        takeRef = takeDatabase.getReference();

        driverEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        String[] parts2 = driverEmail.split("@");

        driverID = parts2[0];




        View v = inflater.inflate(R.layout.fragment_offline_take_payment, container, false);
        takePaymentBtn = v.findViewById(R.id.offlineTakeBtn);
        toSpin = v.findViewById(R.id.offlineTakeToSpin);
        fromSpin = v.findViewById(R.id.offlineTakeFromSpin);
        timeSpin = v.findViewById(R.id.offlineTakeTimeSpin);

        offlineTakeEmailET = v.findViewById(R.id.offlineTakeEmailText);
        offlineTakePasswordET = v.findViewById(R.id.offlineTakePasswordText);


        calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_WEEK);
        // 7 saturday 1 sunday.
        if ((day != 7 & day != 1)) {
            isWeekday = true;
        } else {
            isWeekday = false;
        }

        takeRef.child("Routes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> fromSet = new HashSet<>();

                for (DataSnapshot eachFromToSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> to = new ArrayList<>();
                    String[] fromTo = eachFromToSnapshot.getKey().toString().split("-");

                    String from = fromTo[0];
                    fromSet.add(from);


                    if (fromToTable.get(from) != null) {
                        to = fromToTable.get(from);
                        to.add(fromTo[1]);
                        fromToTable.put(from, to);

                    } else {
                        to.add(fromTo[1]);
                        fromToTable.put(from, to);

                    }


                }


                List<String> fromList = new ArrayList<>(fromSet);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, fromList);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                fromSpin.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fromSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = fromSpin.getSelectedItem().toString();


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


        toSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text1 = fromSpin.getSelectedItem().toString();
                String text2 = toSpin.getSelectedItem().toString();

                takeRef.child("Routes").child(text1 + "-" + text2).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        route = dataSnapshot.getValue(Route.class);
                        ArrayList<String> timeList = new ArrayList<>();
                        if (isWeekday) {
                            timeList.addAll(route.getWeekdayTimes().keySet());
                            Collections.sort(timeList);
                        } else {
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

        takePaymentBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(final View v) {


                final String handleFrom = fromSpin.getSelectedItem().toString();
                final String handleTo = toSpin.getSelectedItem().toString();
                final String handleTime = timeSpin.getSelectedItem().toString();

                final String offlineTakeEmail = offlineTakeEmailET.getText().toString();
                final String offlineTakePassword = offlineTakePasswordET.getText().toString();

                String[] parts = offlineTakeEmail.split("@");

                final String customerID = parts[0];

                if (offlineTakeEmail.equals("") || offlineTakePassword.equals("")) {
                    Toast.makeText(getContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(offlineTakeEmail, offlineTakePassword);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                takeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        final DataSnapshot myDataSnaphShot = dataSnapshot;

                                        takeBalance = (long) dataSnapshot.child("Customers").child(customerID).child("balance").getValue();
                                        final Route myRoute = dataSnapshot.child("Routes").child(handleFrom + "-" + handleTo).getValue(Route.class);
                                        List<String> customersWithReservation = new ArrayList<String>();

                                        if (isWeekday) {

                                            takeAvailability = myRoute.getWeekdayTimesValue(handleTime).getAvailability();
                                            takePrice = (long) myRoute.getWeekdayTimesValue(handleTime).getPrice();

                                            customersWithReservation.addAll(myRoute.getWeekdayTimesValue(handleTime).getUsers().keySet());

                                        } else {

                                            takeAvailability = myRoute.getWeekendTimesValue(handleTime).getAvailability();
                                            takePrice = (long) myRoute.getWeekendTimesValue(handleTime).getPrice();

                                            customersWithReservation.addAll(myRoute.getWeekendTimesValue(handleTime).getUsers().keySet());

                                        }


                                        if (takeAvailability <= 0) {

                                            Toast.makeText(getContext(), "No available seats!", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();

                                            driverPassword = (String) myDataSnaphShot.child("Drivers").child(driverID).child("password").getValue();
                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(driverEmail, driverPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    Toast.makeText(getContext(), "Driver signed back.", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        } else if (takeBalance < takePrice) {

                                            Toast.makeText(getContext(), "Insufficient balance!", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();

                                            driverPassword = (String) myDataSnaphShot.child("Drivers").child(driverID).child("password").getValue();
                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(driverEmail, driverPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    Toast.makeText(getContext(), "Driver signed back.", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else if (customersWithReservation.contains(customerID)) {

                                            Toast.makeText(getContext(), "Thank you for reservation, " + customerID, Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().signOut();

                                            driverPassword = (String) myDataSnaphShot.child("Drivers").child(driverID).child("password").getValue();
                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(driverEmail, driverPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    Toast.makeText(getContext(), "Driver signed back.", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else {

                                            takeNewBalance = takeBalance - takePrice;

                                            takeNewAvailability = (int) takeAvailability - 1;


                                            takeRef.child("Customers").child(customerID).child("balance").setValue(takeNewBalance, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                    if (isWeekday) {

                                                        myRoute.getWeekdayTimesValue(handleTime).setAvailability(takeNewAvailability);
                                                        myRoute.getWeekdayTimesValue(handleTime).getUsers().put(customerID, customerID);

                                                        takeRef.child("Routes").child(handleFrom + "-" + handleTo).setValue(myRoute, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                                Toast.makeText(getContext(), customerID, Toast.LENGTH_SHORT).show();
                                                                offlineTakeEmailET.setText("");
                                                                offlineTakePasswordET.setText("");
                                                                FirebaseAuth.getInstance().signOut();

                                                                driverPassword = (String) myDataSnaphShot.child("Drivers").child(driverID).child("password").getValue();
                                                                FirebaseAuth.getInstance().signInWithEmailAndPassword(driverEmail, driverPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        Toast.makeText(getContext(), "Driver signed back.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });


                                                            }
                                                        });


                                                    } else {

                                                        myRoute.getWeekendTimesValue(handleTime).setAvailability(takeNewAvailability);
                                                        myRoute.getWeekendTimesValue(handleTime).getUsers().put(customerID, customerID);

                                                        takeRef.child("Routes").child(handleFrom + "-" + handleTo).setValue(myRoute, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                                Toast.makeText(getContext(), customerID, Toast.LENGTH_SHORT).show();
                                                                offlineTakeEmailET.setText("");
                                                                offlineTakePasswordET.setText("");
                                                                FirebaseAuth.getInstance().signOut();

                                                                driverPassword = (String) myDataSnaphShot.child("Drivers").child(driverID).child("password").getValue();
                                                                FirebaseAuth.getInstance().signInWithEmailAndPassword(driverEmail, driverPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        Toast.makeText(getContext(), "Driver signed back.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                            }
                                                        });


                                                    }

                                                }
                                            });

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });


                            } else {

                                Toast.makeText(getContext(), "Wrong password or email!", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });


                }


            }
        });


        return v;
    }


    @Override
    public void onPause(){
        super.onPause();


    }





}

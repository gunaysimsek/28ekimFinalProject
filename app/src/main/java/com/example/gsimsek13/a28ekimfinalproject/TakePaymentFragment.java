package com.example.gsimsek13.a28ekimfinalproject;


import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakePaymentFragment extends Fragment implements ZXingScannerView.ResultHandler {

    Button takePaymentBtn;
    private ZXingScannerView mScannerView;
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

    public String takeFromBtnPressed;
    public String takeToBtnPressed;
    public String takeTimeBtnPressed;

    public TakePaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("QR Payment");

        takeDatabase = FirebaseDatabase.getInstance();
        takeRef = takeDatabase.getReference();


        View v = inflater.inflate(R.layout.fragment_take_payment, container, false);


        takePaymentBtn = v.findViewById(R.id.takeQrBtn);
        toSpin = v.findViewById(R.id.takeToSpin);
        fromSpin = v.findViewById(R.id.takeFromSpin);
        timeSpin = v.findViewById(R.id.takeTimeSpin);

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

        mScannerView = new ZXingScannerView(getContext());
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.INTERNET, android.Manifest.permission.CAMERA
                }, 1);

            }
        }

        takePaymentBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(final View v) {

             //   FrameLayout contentFrameLayout = getActivity().findViewById(R.id.driver_content_frame);
                FrameLayout contentFrameLayout = getActivity().findViewById(R.id.driver_content_frame);
                contentFrameLayout.removeAllViews();
                contentFrameLayout.addView(mScannerView);

                takeFromBtnPressed = fromSpin.getSelectedItem().toString();
                takeToBtnPressed = toSpin.getSelectedItem().toString();
                takeTimeBtnPressed = timeSpin.getSelectedItem().toString();


                mScannerView.setResultHandler(TakePaymentFragment.this);
                mScannerView.startCamera();


            }
        });

        return v;
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        mScannerView.stopCameraPreview();

        final String customerID = result.getText();

        final String handleFrom = takeFromBtnPressed;
        final String handleTo = takeToBtnPressed;
        final String handleTime = takeTimeBtnPressed;


        takeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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


                }


                if (takeAvailability <= 0) {
                    Toast.makeText(getContext(), "No available seats!", Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(TakePaymentFragment.this);
                } else if (takeBalance < takePrice) {
                    Toast.makeText(getContext(), "Insufficient balance!", Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(TakePaymentFragment.this);
                } else if (customersWithReservation.contains(customerID)){

                    Toast.makeText(getContext(), "Thank you for reservation, "+customerID, Toast.LENGTH_SHORT).show();
                    mScannerView.resumeCameraPreview(TakePaymentFragment.this);

                }

                else {
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
                                        // SystemClock.sleep(1000);
                                        mScannerView.resumeCameraPreview(TakePaymentFragment.this);

                                    }
                                });



                            } else {

                                myRoute.getWeekendTimesValue(handleTime).setAvailability(takeNewAvailability);
                                myRoute.getWeekendTimesValue(handleTime).getUsers().put(customerID, customerID);

                                takeRef.child("Routes").child(handleFrom + "-" + handleTo).setValue(myRoute, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        Toast.makeText(getContext(), customerID, Toast.LENGTH_SHORT).show();
                                        // SystemClock.sleep(1000);
                                        mScannerView.resumeCameraPreview(TakePaymentFragment.this);

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


    }


}

package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView nametv;
    TextView surnametv;
    TextView mailtv;
    TextView phonetv;
    TextView balancetv;

    private FirebaseDatabase database ;
    private DatabaseReference myRef;

    private String[] parts;

    private Customer customer;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_profile,container,false );


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        nametv = (TextView) v.findViewById(R.id.profileNameTxt);
        surnametv = (TextView) v.findViewById(R.id.profileSurnameTxt);
        mailtv = (TextView) v.findViewById(R.id.profileEmailTxt);
        phonetv = (TextView) v.findViewById(R.id.profilePhoneTxt);
        balancetv = v.findViewById(R.id.profileBalanceTxt);

        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");


        myRef.child("Customers").child(parts[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               customer  =  dataSnapshot.getValue(Customer.class);

                nametv.setText(customer.name);
                surnametv.setText(customer.surname);
                mailtv.setText(customer.email);
                phonetv.setText(""+customer.phoneNumber);
                balancetv.setText(""+customer.balance);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return v;
    }

}

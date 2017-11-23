package com.example.gsimsek13.a28ekimfinalproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    EditText nametv;
    EditText surnametv;
    TextView mailtv;
    EditText phonetv;
    TextView balancetv;
    Button updateProfileBtn;
    Button topUpProfileBtn;
    Button changePasswordProfileBtn;

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

        nametv =  v.findViewById(R.id.profileNameTxt);
        surnametv = v.findViewById(R.id.profileSurnameTxt);
        mailtv =  v.findViewById(R.id.profileEmailTxt);
        phonetv =  v.findViewById(R.id.profilePhoneTxt);
        balancetv = v.findViewById(R.id.profileBalanceTxt);
        updateProfileBtn = v.findViewById(R.id.profileUpdateBtn);
        topUpProfileBtn = v.findViewById(R.id.profileTopUpBtn);
        changePasswordProfileBtn = v.findViewById(R.id.profileChangePasswordBtn);

        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");


        myRef.child("Customers").child(parts[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               customer  =  dataSnapshot.getValue(Customer.class);

                nametv.setText(customer.name);
                surnametv.setText(customer.surname);
                mailtv.setText(customer.email);
                phonetv.setText(""+customer.phoneNumber);
                balancetv.setText("₺"+customer.balance);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        updateProfileBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Double updatedPhoneNo = Double.parseDouble( phonetv.getText().toString());
                String updatedName = nametv.getText().toString();
                String updatedSurname = surnametv.getText().toString();


                Customer updatedCustomer = new Customer(customer.id ,customer.role,updatedName,updatedSurname, customer.email, updatedPhoneNo, customer.balance);
                myRef.child("Customers").child(parts[0]).setValue(updatedCustomer, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        Toast.makeText(getContext(),"Customer's info is updated!",Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

        topUpProfileBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Fragment fragment = new TopUpFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        changePasswordProfileBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Fragment fragment = new ChangePasswordFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });



        return v;
    }

}

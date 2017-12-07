package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class TopUpFragment extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    private long oldAmount;
    private EditText topUpAmountET;

    private Button topUpSendBtn;
    private String[] parts;


    public TopUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_top_up, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        topUpAmountET = v.findViewById(R.id.topUpCreditAmount);
        topUpSendBtn = v.findViewById(R.id.topUpSendBtn);
        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");

        myRef.child("Customers").child(parts[0]).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                oldAmount = (long) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        topUpSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topUpAmountET.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter amount.", Toast.LENGTH_SHORT).show();
                } else {
                    long topUpAmount = Long.parseLong(topUpAmountET.getText().toString());
                    long newBalance = oldAmount + topUpAmount;

                    myRef.child("Customers").child(parts[0]).child("balance").setValue(newBalance, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Toast.makeText(getContext(), "Top up is successful!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

        return v;
    }

}

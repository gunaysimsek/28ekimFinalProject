package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
public class SendMoneyFragment extends Fragment {

    private TextView sendMoneyBalanceTV;
    private EditText sendMoneyFriendET;
    private EditText sendMoneyAmountET;
    private ImageButton sendMoneySendBtn;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String[] parts;
    private Customer customer;

    private long oldAmount;

    private long sendMoneyFriendBalance;

    private long sendMoneyAmount;

    private String sendMoneyFriendID;

    public SendMoneyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_send_money, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        sendMoneyBalanceTV = v.findViewById(R.id.sendMoneyBalanceTV);
        sendMoneyFriendET = v.findViewById(R.id.sendMoneyFriendET);
        sendMoneyAmountET = v.findViewById(R.id.sendMoneyAmountET);
        sendMoneySendBtn = v.findViewById(R.id.sendMoneySendBtn);

        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");

        myRef.child("Customers").child(parts[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                customer = dataSnapshot.getValue(Customer.class);

                sendMoneyBalanceTV.setText("₺" + customer.balance);
                oldAmount = (long) customer.balance;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendMoneySendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMoneyAmount = Long.parseLong(sendMoneyAmountET.getText().toString());
                sendMoneyFriendID = sendMoneyFriendET.getText().toString();

                if (sendMoneyAmountET.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter amount.", Toast.LENGTH_SHORT).show();
                } else if (sendMoneyFriendID.equals("")) {
                    Toast.makeText(getContext(), "Please enter your friend's ID.", Toast.LENGTH_SHORT).show();
                } else {

                    myRef.child("Customers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Boolean isNotExist = true;


                            customer = dataSnapshot.child(parts[0]).getValue(Customer.class);
                            oldAmount = (long) customer.balance;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                if (snapshot.getKey().equals(sendMoneyFriendID) ) {

                                    isNotExist = false;

                                    sendMoneyFriendBalance = (long) snapshot.child("balance").getValue();

                                }
                            }

                            if (oldAmount < sendMoneyAmount) {
                                Toast.makeText(getContext(), "Insufficient balance!", Toast.LENGTH_SHORT).show();
                            } else if (isNotExist) {

                                Toast.makeText(getContext(), "Please check your friend's ID.", Toast.LENGTH_SHORT).show();

                            } else {
                                long newBalance = oldAmount - sendMoneyAmount;

                                final long newFriendBalance = sendMoneyFriendBalance + sendMoneyAmount;

                                customer.balance = (double) newBalance;
                                myRef.child("Customers").child(parts[0]).setValue(customer, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        myRef.child("Customers").child(sendMoneyFriendID).child("balance").setValue(newFriendBalance, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                Toast.makeText(getContext(), "Send is successful!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
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
        });


        return v;
    }

}

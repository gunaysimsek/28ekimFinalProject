package com.example.gsimsek13.a28ekimfinalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "adasd";
    private FirebaseAuth mAuth;

    private String email;
    private String password;
    private String passwordValid;

    private String name;
    private String surname;
    private Double phoneNo;


    private EditText emailText;
    private EditText passwordText;
    private EditText passwordValidText;

    private EditText nameText;
    private EditText surnameText;
    private EditText phoneText;

    private String[] myParts;


    private FirebaseDatabase database ;
    private DatabaseReference myRef;

    private Customer customer;

    private final String schoolEmailDomain =  "ku.edu.tr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();



        emailText   = findViewById(R.id.signUpEmailText);
        passwordText = findViewById(R.id.signUpPasswordText);
        passwordValidText = findViewById(R.id.signUpPasswordValidText);

        nameText =  findViewById(R.id.signUpNameText);
        surnameText = findViewById(R.id.signUpSurnameText);
        phoneText = findViewById(R.id.signUpPhoneText);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();




    }

    private void sendVerificationEmail()
    {


        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }
                        else
                        {

                           Toast.makeText(SignUpActivity.this,"Verification maili gönderilemedi.",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void signUpSend(View view){


            phoneNo = Double.parseDouble( phoneText.getText().toString());


            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            passwordValid = passwordValidText.getText().toString();

            name = nameText.getText().toString();
            surname = surnameText.getText().toString();


            customer = new Customer(1, 1, name, surname, email, phoneNo, 0.0);

        String[] parts = email.split("@");

        String part1 = parts[0];
        String part2 = parts[1];



        /* if(!(part2.equals(schoolEmailDomain))){

            Toast.makeText(SignUpActivity.this ,"You need to have ku email to sign up!", Toast.LENGTH_LONG).show();


        }else */if(!(password.equals(passwordValid))){

            Toast.makeText(SignUpActivity.this, "Passwords don't match!",Toast.LENGTH_LONG).show();

        }else if(email.equals("") || password.equals("") || name.equals("") || surname.equals("") )
        {
            Toast.makeText(SignUpActivity.this, "Please fill all necessary text fields!",Toast.LENGTH_LONG).show();

        }else{

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                            if (!task.isSuccessful()) {


                            }else if(task.isSuccessful()){

                                myParts = email.split("@");

                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                   // (int id, int role, String name, String surname, String email, double phoneNumber,double balance)
                                                    customer = new Customer(12,1,name,surname,email, phoneNo, 0.0);
                                                    myRef.child("Customers").child(myParts[0]).setValue(customer, new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                                        }
                                                    });

                                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                    startActivity(intent);

                                                }
                                                else
                                                {


                                                }
                                            }
                                        });

                            }


                        }
                    });

        }
    }

}

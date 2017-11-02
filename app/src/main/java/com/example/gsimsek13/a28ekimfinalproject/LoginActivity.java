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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "as";
    private FirebaseAuth mAuth;
    private String email;
    private String password;

    private EditText emailText;
    private EditText passwordText;

    private Customer customer;




    private FirebaseAuth.AuthStateListener mAuthListener;

    public void signInClicked(View view){

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Auth Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                      /*  else {

                            checkIfEmailVerified();
                        }*/

                        // ...
                    }
                });

    }

    private void checkIfEmailVerified()
    {
     //   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (mAuth.getCurrentUser().isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), CustomerMainActivity.class);
            startActivity(intent);

        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(LoginActivity.this, "Please verify your account to continue.", Toast.LENGTH_SHORT).show();
           // mAuth.getCurrentUser().sendEmailVerification();
            mAuth.signOut();
            //recreate();

            //restart this activity

        }
    }

    public void signUpClicked(View view){

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

    public void forgetBtnClicked(View view){

        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       /* Intent intent = new Intent(this, CustomerMainActivity.class);

        startActivity(intent);*/



        emailText   = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this,"burdasin"+user.getEmail(),Toast.LENGTH_LONG).show();
                    checkIfEmailVerified();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(LoginActivity.this,"onAuthStateChanged:signed_out",Toast.LENGTH_LONG).show();
                }

            }
        };


    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
package com.example.gsimsek13.a28ekimfinalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email;
    private EditText emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Change Password");
    }


    public void sendBtnClicked(View view) {



        emailTV = findViewById(R.id.forgotEmailTxt);

        email =  emailTV.getText().toString().trim();


        Toast.makeText(ResetPasswordActivity.this, email, Toast.LENGTH_LONG).show();


        mAuth.sendPasswordResetEmail(email);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Reset email is sent!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Wrong e-mail!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}


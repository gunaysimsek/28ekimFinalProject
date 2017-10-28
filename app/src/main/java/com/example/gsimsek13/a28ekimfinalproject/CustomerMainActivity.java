package com.example.gsimsek13.a28ekimfinalproject;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class CustomerMainActivity extends AppCompatActivity   {

    TextView userName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

       // Toast.makeText(getApplicationContext(), getIntent().getStringExtra("deneme"), Toast.LENGTH_SHORT).show();
        userName = (TextView) findViewById(R.id.myUserName);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            userName.setText(name + ", " + email + ", " + photoUrl + ", " + uid + "." );
        }

    }

    public void goQR(View view){

        Intent intent = new Intent(this, QRActivity.class);

        startActivity(intent);

    }
}
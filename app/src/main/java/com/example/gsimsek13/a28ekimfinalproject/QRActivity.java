package com.example.gsimsek13.a28ekimfinalproject;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRActivity extends Activity implements ZXingScannerView.ResultHandler  {
    private ZXingScannerView mScannerView;

    String deneme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);


        mScannerView = new ZXingScannerView(this);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.INTERNET, android.Manifest.permission.CAMERA
                }, 1);
               // return;
            }
        }

    }
    public void goMenuClick(View view){

        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();


    }




    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result result) {
        //Do anything with result here :D
        Log.w("handleResult", result.getText());
        deneme = result.getText();

        Toast.makeText(this, deneme,
                Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();

        //Intent intent = new Intent(this, CustomerMainActivity.class);
     //   intent.putExtra("deneme",deneme);
       // startActivity(intent);







       /* FirebaseDatabase.getInstance().getReference("restNames")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            if(restoranty.equals(snapshot.getValue(String.class ))){
                                //alertDialog.show();
                                Intent myIntent = getIntent();
                                myUser = (User) myIntent.getSerializableExtra(LoginActivity.User);

                                Intent niyet = new Intent(QRActivity.this, MenuActivity.class);
                                niyet.putExtra(LoginActivity.User,myUser);
                                startActivity(niyet);
                            }
                            //Resume scanning
                            else{

                                Toast.makeText(getApplicationContext(), "wrong QR", Toast.LENGTH_SHORT).show();
                                SystemClock.sleep(1000);
                                mScannerView.resumeCameraPreview(QRActivity.this);
                            }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }); */




    }
}

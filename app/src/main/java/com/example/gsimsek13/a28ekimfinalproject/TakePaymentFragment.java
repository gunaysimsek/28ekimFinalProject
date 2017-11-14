package com.example.gsimsek13.a28ekimfinalproject;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakePaymentFragment extends Fragment {

    Button takePaymentBtn;
    private ZXingScannerView mScannerView;


    public TakePaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_take_payment,container,false );
        takePaymentBtn = v.findViewById(R.id.takeQrBtn);

        takePaymentBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), QRActivity.class);

                startActivity(intent);

            }
        });

        return v;
    }



}

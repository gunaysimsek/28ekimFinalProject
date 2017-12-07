package com.example.gsimsek13.a28ekimfinalproject;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;


/**
 * A simple {@link Fragment} subclass.
 */
public class QRFragment extends Fragment {


    public QRFragment() {
        // Required empty public constructor
    }

    ImageView qrIW;
    Thread thread;
    private String[] parts;
    public final static int QRcodeWidth = 500;

    private Bitmap bm;

    DatabaseHelper myDb ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qr, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Show QR");

        myDb =  new DatabaseHelper(getContext());
        qrIW = v.findViewById(R.id.qrIW);

        Log.wtf("asad", FirebaseAuth.getInstance().getCurrentUser().getEmail() + "asdajfgşsgjaşg burdaasiin");


        parts = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@");

        String customerID = parts[0];

        /*QRCodeWriter writer = new QRCodeWriter();
        try {

            BitMatrix bitMatrix = writer.encode(parts[0], BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrIW.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }*/

        bm = myDb.getEntry(customerID);

        if (bm != null) {

            qrIW.setImageBitmap(bm);

        } else {
            try {
                // generate a 150x150 QR code
                bm = TextToImageEncode(customerID);

                if (bm != null) {
                    qrIW.setImageBitmap(bm);
                    myDb.addEntry(customerID, DbBitmapUtility.getBytes(bm));
                }
            } catch (WriterException e) {

            }

        }


        return v;
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


}

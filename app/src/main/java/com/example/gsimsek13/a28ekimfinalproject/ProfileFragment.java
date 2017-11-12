package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView nametv;
    TextView surnametv;
    TextView mailtv;
    TextView phonetv;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile,container,false );


        nametv = (TextView) v.findViewById(R.id.profileNameTxt);
        surnametv = (TextView) v.findViewById(R.id.profileSurnameTxt);
        mailtv = (TextView) v.findViewById(R.id.profileEmailTxt);
        phonetv = (TextView) v.findViewById(R.id.profilePhoneTxt);


        User user = new User();
        user.name = "Gunay";
        user.surname = "Simsek";
        user.email = "gunayberkaysimsek@gmail.com";
        user.phoneNumber = 538392;




        nametv.setText(user.name);
        surnametv.setText(user.surname);
        mailtv.setText(user.email);
        phonetv.setText(""+user.phoneNumber);


        return v;
    }

}

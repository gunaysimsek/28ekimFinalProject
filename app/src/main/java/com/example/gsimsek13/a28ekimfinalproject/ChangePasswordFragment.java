package com.example.gsimsek13.a28ekimfinalproject;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    private FirebaseUser user;

    private Button changePasswordBtn;
    private EditText changePasswordPW;
    private EditText changePasswordValidPW;
    private EditText changePasswordOldPW;

    private String password;
    private String repassword;
    private String oldPassword;


    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        changePasswordBtn = v.findViewById(R.id.changePasswordBtn);
        changePasswordPW = v.findViewById(R.id.changePasswordPW);
        changePasswordValidPW = v.findViewById(R.id.changePasswordValidPW);
        changePasswordOldPW = v.findViewById(R.id.changePasswordOldPW);


        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = changePasswordPW.getText().toString();
                repassword = changePasswordValidPW.getText().toString();
                oldPassword = changePasswordOldPW.getText().toString();

                if (password.equals("") || repassword.equals("") || oldPassword.equals("")) {
                    Toast.makeText(getContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else if (!(password.equals(repassword))) {
                    Toast.makeText(getContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                } else {

                    user = FirebaseAuth.getInstance().getCurrentUser();


                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), oldPassword);


                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Error password not updated", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), "Old password is incorrect!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                }
            }
        });


        return v;
    }

}

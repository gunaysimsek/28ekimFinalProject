package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gunay on 30/10/2017.
 */

public class User implements Serializable{

    public int id;
    public int role;
    public String name;
    public String surname;
    public String email;
    public String password;
    public double phoneNumber;





    public User(){
        this.id = -1;
        this.role = -1;
        this.name = "";
        this.surname = "";
        this.email = "";
        this.password = "";
        this.phoneNumber = -1;


    }

    public User(int id, int role, String name, String surname, String email, String password, int phoneNumber){
        this.id = id;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;


    }

}

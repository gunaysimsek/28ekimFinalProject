package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;

/**
 * Created by Gunay on 31/10/2017.
 */

public class Customer extends User implements Serializable {

    double balance;
    String Gunay = "deneme";

    public Customer(){
        super();
        this.balance = 0.0;

    }

    public Customer(int id, int role, String name, String surname, String email, double phoneNumber, double balance){
        super(id,role,name,surname,email,phoneNumber);
        this.balance = balance;

    }
}

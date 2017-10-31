package com.example.gsimsek13.a28ekimfinalproject;

/**
 * Created by Gunay on 31/10/2017.
 */

public class Customer extends User {

    double balance;

    public Customer(){
        super();
        this.balance = 0.0;

    }

    public Customer(int id, int role, String name, String surname, String email, String password, int phoneNumber, double balance){
        super(id,role,name,surname,email,password,phoneNumber);
        this.balance = balance;

    }
}

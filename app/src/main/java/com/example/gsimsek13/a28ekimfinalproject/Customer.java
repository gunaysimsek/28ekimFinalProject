package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Gunay on 31/10/2017.
 */

public class Customer extends User implements Serializable {

    double balance;
    private HashMap<String,Reservations> reservations;

    public Customer(){
        super();
        this.balance = 0.0;

    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public HashMap<String, Reservations> getReservations() {
        return reservations;
    }

    public void setReservations(HashMap<String, Reservations> reservations) {
        this.reservations = reservations;
    }

    public Customer(int id, int role, String name, String surname, String email, String phoneNumber, double balance,HashMap<String,Reservations> reservations){
        super(id,role,name,surname,email,phoneNumber);
        this.balance = balance;
        this.reservations = reservations;

    }
}

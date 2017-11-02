package com.example.gsimsek13.a28ekimfinalproject;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Driver extends User {
    private double rating;

    public Driver(){
        super();
        this.rating = 0.0;

    }

    public Driver(int id, int role, String name, String surname, String email, double phoneNumber, double rating){
        super(id,role,name,surname,email,phoneNumber);
        this.rating = rating;

    }
}

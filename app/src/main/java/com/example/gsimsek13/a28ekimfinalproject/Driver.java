package com.example.gsimsek13.a28ekimfinalproject;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Driver extends User {
    private double rating;
    private double latitude;
    private double longitude;

    public Driver(){
        super();
        this.rating = 0.0;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {

        return rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Driver(int id, int role, String name, String surname, String email, double phoneNumber, double rating, double latitude, double longitude){
        super(id,role,name,surname,email,phoneNumber);
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

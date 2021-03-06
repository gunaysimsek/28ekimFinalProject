package com.example.gsimsek13.a28ekimfinalproject;

import java.util.HashMap;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Driver extends User {
    private double rating;
    private double latitude;
    private double longitude;
    private int raters;
    //private boolean onWork;
    private HashMap<String,DriverRoutes> routes;
    private String password;


    public Driver(){

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

    public int getRaters() {
        return raters;
    }

    public void setRaters(int raters) {
        this.raters = raters;
    }

    public Driver(int id, int role, String name, String surname, String email, String phoneNumber, double rating, double latitude, double longitude,int raters,HashMap<String, DriverRoutes> routes, String password){
        super(id,role,name,surname,email,phoneNumber);
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.raters = raters;
        this.routes = routes;
        this.password = password;
    }

    public HashMap<String, DriverRoutes> getRoutes() {
        return routes;
    }

    public void setRoutes(HashMap<String, DriverRoutes> routes) {
        this.routes = routes;
    }
    public DriverRoutes getRouteValue(String route){
        return routes.get(route);
    }
    //public boolean isOnWork() {
    //   return onWork;
    //}

    //public void setOnWork(boolean onWork) {
    //   this.onWork = onWork;
    //}

}

package com.example.gsimsek13.a28ekimfinalproject;

import java.util.HashMap;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Driver extends User {
    private double rating;
    private double latitude;
    private double longitude;
    private boolean onWork;
    private HashMap<String,DriverRoutes> routes;



    public Driver(int id, int role, String name, String surname, String email, double phoneNumber, double rating, double latitude, double longitude, boolean onWork, HashMap<String, DriverRoutes> routes) {

        super(id, role, name, surname, email, phoneNumber);
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.onWork = onWork;
        this.routes = routes;
    }

    public double getRating() {

        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public HashMap<String, DriverRoutes> getRoutes() {
        return routes;
    }

    public void setRoutes(HashMap<String, DriverRoutes> routes) {
        this.routes = routes;
    }
    private DriverRoutes getRouteValue(String route){
        return routes.get(route);
    }
    public boolean isOnWork() {
        return onWork;
    }

    public void setOnWork(boolean onWork) {
        this.onWork = onWork;
    }
}

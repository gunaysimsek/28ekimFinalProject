package com.example.gsimsek13.a28ekimfinalproject;

import java.util.List;

/**
 * Created by Efehan on 24.11.2017.
 */

public class Times {
    int Availability;
    String Driver;
    double Price;
    List<String> Users;

    public Times(int availability, String driver, double price, List<String> users) {
        Availability = availability;
        Driver = driver;
        Price = price;
        Users = users;
    }

    public int getAvailability() {
        return Availability;
    }

    public void setAvailability(int availability) {
        Availability = availability;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public List<String> getUsers() {
        return Users;
    }

    public void setUsers(List<String> users) {
        Users = users;
    }
}

package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Efehan on 24.11.2017.
 */

public class Times implements Serializable {
    int availability;
    String driver;
    double price;
    String shuttle_Time;
    List<String> users;

    public Times(int availabilityy, String driverr, double pricee,String shuttle_Timee, List<String> userss) {
        availability = availabilityy;
        driver = driverr;
        price = pricee;
        shuttle_Time = shuttle_Timee;
        users = userss;
    }

    public Times(){}
    public String getShuttle_Time() {
        return this.shuttle_Time;
    }

    public void setShuttle_Time(String shuttle_Time) {
        this.shuttle_Time = shuttle_Time;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

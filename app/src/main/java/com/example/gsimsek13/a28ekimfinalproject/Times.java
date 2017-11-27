package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Efehan on 24.11.2017.
 */

public class Times implements Serializable {
    int availability;
    String driver;
    double price;
    String shuttle_Time;
    HashMap<String,String> users;

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

    public String getShuttle_Time() {
        return shuttle_Time;
    }

    public void setShuttle_Time(String shuttle_Time) {
        this.shuttle_Time = shuttle_Time;
    }

    public HashMap<String, String> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, String> users) {
        this.users = users;
    }
    public String getCustomerValue(String customername){
        return users.get(customername);
    }

    @Override
    public String toString() {
        return "Times{" +
                "availability=" + availability +
                ", driver='" + driver + '\'' +
                ", price=" + price +
                ", shuttle_Time='" + shuttle_Time + '\'' +
                ", users=" + users +
                '}';
    }

    public Times(){}
    public Times(int availability, String driver, double price, String shuttle_Time, HashMap<String, String> users) {
        this.availability = availability;
        this.driver = driver;
        this.price = price;
        this.shuttle_Time = shuttle_Time;
        this.users = users;
    }
}

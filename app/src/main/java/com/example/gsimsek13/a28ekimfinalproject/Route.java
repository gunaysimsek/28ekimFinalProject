package com.example.gsimsek13.a28ekimfinalproject;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Route {

    private int id;
    private String from;
    private String to;
    private double cost;
    private Time time;
    private Date date;
    private Shuttle shuttle;
    private Driver driver;


    public Route(int id, String from, String to, double cost, Time time, Date date, Shuttle shuttle, Driver driver) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.time = time;
        this.date = date;
        this.shuttle = shuttle;
        this.driver = driver;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Shuttle getShuttle() {
        return shuttle;
    }

    public void setShuttle(Shuttle shuttle) {
        this.shuttle = shuttle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}

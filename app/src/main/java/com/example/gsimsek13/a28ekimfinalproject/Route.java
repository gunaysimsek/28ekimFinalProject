package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Route implements Serializable {


    private String from;
    private String to;
    private HashMap<String,Times> weekdayTimes;
    private HashMap<String,Times> weekendTimes;

    public Route(){}

    public Route(String from, String to, HashMap<String, Times> weekdayTimes, HashMap<String, Times> weekendTimes) {
        this.from = from;
        this.to = to;
        this.weekdayTimes = weekdayTimes;
        this.weekendTimes = weekendTimes;
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

    public HashMap<String, Times> getWeekdayTimes() {
        return weekdayTimes;
    }
    public Times getWeekdayTimesValue(String time){
        return weekdayTimes.get(time);
    }
    public Times getWeekendTimesValue(String time){
        return weekendTimes.get(time);
    }

    public void setWeekdayTimes(HashMap<String, Times> weekdayTimes) {
        this.weekdayTimes = weekdayTimes;
    }

    public HashMap<String, Times> getWeekendTimes() {
        return weekendTimes;
    }

    public void setWeekendTimes(HashMap<String, Times> weekendTimes) {
        this.weekendTimes = weekendTimes;
    }

    @Override
    public String toString() {
        return "Route{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", weekdayTimes=" + weekdayTimes +
                ", weekendTimes=" + weekendTimes +
                '}';
    }
}

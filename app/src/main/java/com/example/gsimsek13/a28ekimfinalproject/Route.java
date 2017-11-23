package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Route implements Serializable {


    private String from;
    private String to;
    private List<Times> weekdayTimes;
    private List<Times> weekendTimes;

    public Route(){}

    public Route(String fromm, String too, List<Times> weekdayTimess, List<Times> weekendTimess) {
        from = fromm;
        to = too;
        weekdayTimes = weekdayTimess;
        weekendTimes = weekendTimess;
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

    public List<Times> getWeekdayTimes() {
        return weekdayTimes;
    }

    public void setWeekdayTimes(List<Times> weekdayTimes) {
        this.weekdayTimes = weekdayTimes;
    }

    public List<Times> getWeekendTimes() {
        return weekendTimes;
    }

    public void setWeekendTimes(List<Times> weekendTimes) {
        this.weekendTimes = weekendTimes;
    }
}

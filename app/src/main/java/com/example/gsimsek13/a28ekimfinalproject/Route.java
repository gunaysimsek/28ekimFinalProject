package com.example.gsimsek13.a28ekimfinalproject;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Route{


    private String From;
    private String To;
    private List<Times> WeekdayTimes;
    private List<Times> WeekendTimes;

    public Route(String from, String to, List<Times> weekdayTimes, List<Times> weekendTimes) {
        From = from;
        To = to;
        WeekdayTimes = weekdayTimes;
        WeekendTimes = weekendTimes;
    }

    public String getFrom() {

        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public List<Times> getWeekdayTimes() {
        return WeekdayTimes;
    }

    public void setWeekdayTimes(List<Times> weekdayTimes) {
        WeekdayTimes = weekdayTimes;
    }

    public List<Times> getWeekendTimes() {
        return WeekendTimes;
    }

    public void setWeekendTimes(List<Times> weekendTimes) {
        WeekendTimes = weekendTimes;
    }
}

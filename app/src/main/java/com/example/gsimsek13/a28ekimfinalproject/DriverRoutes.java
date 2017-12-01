package com.example.gsimsek13.a28ekimfinalproject;

import java.util.HashMap;

/**
 * Created by Efehan on 1.12.2017.
 */

public class DriverRoutes {

    private String driverFrom;
    private String driverTo;
    private HashMap<String,String> weekdayTimeList;
    private HashMap<String,String> weekendTimeList;
    public DriverRoutes(){

    }
    public DriverRoutes(String driverFrom, String driverTo, HashMap<String, String> weekdayTimeList, HashMap<String, String> weekendTimeList) {
        this.driverFrom = driverFrom;
        this.driverTo = driverTo;
        this.weekdayTimeList = weekdayTimeList;
        this.weekendTimeList = weekendTimeList;
    }

    public String getDriverFrom() {

        return driverFrom;
    }

    public void setDriverFrom(String driverFrom) {
        this.driverFrom = driverFrom;
    }

    public String getDriverTo() {
        return driverTo;
    }

    public void setDriverTo(String driverTo) {
        this.driverTo = driverTo;
    }

    public HashMap<String, String> getWeekdayTimeList() {
        return weekdayTimeList;
    }

    public void setWeekdayTimeList(HashMap<String, String> weekdayTimeList) {
        this.weekdayTimeList = weekdayTimeList;
    }

    public HashMap<String, String> getWeekendTimeList() {
        return weekendTimeList;
    }

    public void setWeekendTimeList(HashMap<String, String> weekendTimeList) {
        this.weekendTimeList = weekendTimeList;
    }
    private String getWeekdayTimeListValue(String time){
        return weekdayTimeList.get(time);
    }
    private String getWeekendTimeListValue(String time){
        return weekendTimeList.get(time);
    }



}

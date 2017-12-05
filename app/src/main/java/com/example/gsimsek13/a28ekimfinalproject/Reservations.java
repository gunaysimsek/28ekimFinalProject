package com.example.gsimsek13.a28ekimfinalproject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Bartu on 12/2/2017.
 */

public class Reservations implements Serializable {
    String from;
    String to;
    HashMap<String,String> times;

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

    public HashMap<String, String> getTimes() {
        return times;
    }

    public void setTimes(HashMap<String, String> times) {
        this.times = times;
    }


}

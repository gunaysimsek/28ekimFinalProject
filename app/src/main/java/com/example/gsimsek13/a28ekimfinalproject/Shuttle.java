package com.example.gsimsek13.a28ekimfinalproject;

import java.util.Vector;

/**
 * Created by Bartu on 11/2/2017.
 */

public class Shuttle {

    private int id;
    private String plateNumber;
    private Vector location;
    private int availableSeats;
    private int totalSeat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Vector getLocation() {
        return location;
    }

    public void setLocation(Vector location) {
        this.location = location;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(int totalSeat) {
        this.totalSeat = totalSeat;
    }

    public Shuttle(int id, String plateNumber, Vector location, int availableSeats, int totalSeat) {

        this.id = id;
        this.plateNumber = plateNumber;
        this.location = location;
        this.availableSeats = availableSeats;
        this.totalSeat = totalSeat;
    }
}

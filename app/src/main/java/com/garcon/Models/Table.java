package com.garcon.Models;

/**
 * Created by akshaymathur on 7/19/16.
 */
public class Table {

    private String tableID;
    private Boolean available;
    private String name;
    private int number;
    private int seats;

    public Table(String tableID, Boolean available, String name, int number, int seats) {
        this.tableID = tableID;
        this.available = available;
        this.name = name;
        this.number = number;
        this.seats = seats;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}

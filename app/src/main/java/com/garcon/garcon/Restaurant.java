package com.garcon.garcon;
/**
 * Created by Mayank on 6/27/2016.
 */
public class Restaurant{
    String name, location, ID, price, website, hours;
    String[] Type;
    int phone, rating;

    public Restaurant(){
        // empty default constructor, necessary for Firebase to be able to deserialize
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getHours() {
        return hours;
    }
    public void setHours(String name) {
        this.hours = name;
    }
    public String getLoc() {
        return location;
    }
    public void setLoc(String loc) {
        this.location = loc;
    }
    public String getID() {
        return ID;
    }
    public void setID (String unique) {
        this.ID = unique;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String web) {
        this.website = web;
    }
    public String[] getTypes() {
        return Type;
    }
    public void setTypes(String[] types) {
        this.Type = types;
    }
    public int getPhone() {
        return phone;
    }
    public void setPhone(int pnum) {
        this.phone = pnum;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
}
package com.garcon.garcon;
/**
 * Created by Mayank on 6/27/2016.
 */
public class Restaurant{
    String name, location, ID, price, website, hours, Type, phone;
    int rating;

    public Restaurant(){
        // empty default constructor, necessary for Firebase to be able to deserialize JSON
    }
    public String getName() {
        return name;
    }
    public String getHours() {
        return hours;
    }
    public String getLoc() {
        return location;
    }
    public String getID() {
        return ID;
    }
    public String getPrice() {
        return price;
    }
    public String getWebsite() {
        return website;
    }
    public String getTypes() {
        return Type;
    }
    public String getPhone() { return phone; }
    public int getRating() {
        return rating;
    }
}
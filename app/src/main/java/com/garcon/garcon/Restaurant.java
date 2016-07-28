package com.garcon.garcon;

/**
 * Created by Mayank on 6/27/2016.
 */
public class Restaurant {
    String name, location, ID, price, website, hours, Type, phone;
    double rating;

    public Restaurant() {
        // empty default constructor, necessary for Firebase to be able to deserialize JSON
    }

    public String getName() {
        return name;
    }

    public void setName(String newvar) {
        this.name = newvar;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String newvar) {
        this.hours = newvar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String newvar) {
        this.location = newvar;
    }

    public String getID() {
        return ID;
    }

    public void setID(String newvar) {
        this.ID = newvar;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String newvar) {
        this.price = newvar;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String newvar) {
        this.website = newvar;
    }

    public String getTypes() {
        return Type;
    }

    public void setTypes(String newvar) {
        this.Type = newvar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String newvar) {
        this.phone = newvar;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double newvar) {
        this.rating = newvar;
    }
}
package com.garcon.garcon;
/**
 * Created by Mayank on 6/27/2016.
 */
public class Restaurant{
    String name, location, ID, price, website, hours, Type, phone;
    double rating;

    public Restaurant(){
        // empty default constructor, necessary for Firebase to be able to deserialize JSON
    }
    public String getName() {
        return name;
    }
    public String getHours() {
        return hours;
    }
    public String getLocation() {
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
    public double getRating() {
        return rating;
    }

    public void setName(String newvar) {this.name = newvar;}
    public void setHours(String newvar) {this.hours = newvar;}
    public void setLocation(String newvar) {this.location = newvar;}
    public void setID(String newvar) {this.ID = newvar;}
    public void setPrice(String newvar) {this.price = newvar;}
    public void setTypes(String newvar) {this.Type = newvar;}
    public void setWebsite(String newvar) {this.website = newvar;}
    public void setPhone(String newvar) {this.phone = newvar;}
    public void setRating(double newvar) {this.rating = newvar;}
}
package com.garcon.garcon;

/**
 * Created by Mayank on 6/27/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown=true)
public class Restaurant implements Serializable{
    String name="", location="", id="", price="", website="", hours="", type="", phone="";
    String lat, longt;

    public Restaurant() {
        // empty default constructor, necessary for Firebase to be able to deserialize JSON

    }


    public double getLat() {
        return Double.parseDouble(lat);
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public double getLongt() {
        return Double.parseDouble(longt);
    }

    public void setLongt(String longt) {
        this.longt = longt;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String newvar) {
        this.type = newvar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String newvar) {
        this.phone = newvar;
    }

}
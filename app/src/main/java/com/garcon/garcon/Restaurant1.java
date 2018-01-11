package com.garcon.garcon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by siddhiparekh11 on 8/18/17.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Restaurant1 {






        String name, location, id, price, website, hours, type, phone;
        double rating, lat, longt;

        public Restaurant1() {
            // empty default constructor, necessary for Firebase to be able to deserialize JSON
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getid() {
            return id;
        }

        public void setID(String id) {
            this.id = id;
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

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String gettype() {
            return type;
        }

        public void setType(String type) {
            type = type;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLongt() {
            return longt;
        }

        public void setLongt(double longt) {
            this.longt = longt;
        }
    }


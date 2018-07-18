package com.garcon.garcon;

/**
 * Created by Mayank on 6/27/2016.
 *
 * Model class to hold a Restaurant location details.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Calendar;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Restaurant implements Serializable{
    String name="", location="", id="", price="", website="", hours="", type="", phone="";
    String lat, longt;
    String alcohol,ambience,bikeParking, caters,delivery, dogsAllowed, outdoorSeating, parking;
    String reservations,takeout,television,waiterService, wheelchairAccessible, wifi;

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

    public String getParsedHours() {
        String data = hours;
        String[] weekday = data.split("[;]");
        //String header = "Hours:Today "; add to return statements if needed
        String Monday = weekday[0].substring(8);
        String Tuesday = weekday[1].substring(9);
        String Wednesday = weekday[2].substring(11);
        String Thursday = weekday[3].substring(10);
        String Friday = weekday[4].substring(8);
        String Saturday = weekday[5].substring(10);
        String Sunday = weekday[6].substring(8);
        //String finalhour = header + newFriday;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return Monday;
            case Calendar.TUESDAY:
                return Tuesday;
            case Calendar.WEDNESDAY:
                return Wednesday;
            case Calendar.THURSDAY:
                return Thursday;
            case Calendar.FRIDAY:
                return Friday;
            case Calendar.SATURDAY:
                return Saturday;
            case Calendar.SUNDAY:
                return Sunday;
        }
        return null;
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

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }

    public String getAmbience() {
        return ambience;
    }

    public void setAmbience(String ambience) {
        this.ambience = ambience;
    }

    public String getBikeParking() {
        return bikeParking;
    }

    public void setBikeParking(String bikeParking) {
        this.bikeParking = bikeParking;
    }

    public String getCaters() {
        return caters;
    }

    public void setCaters(String caters) {
        this.caters = caters;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDogsAllowed() {
        return dogsAllowed;
    }

    public void setDogsAllowed(String dogsAllowed) {
        this.dogsAllowed = dogsAllowed;
    }

    public String getOutdoorSeating() {
        return outdoorSeating;
    }

    public void setOutdoorSeating(String outdoorSeating) {
        this.outdoorSeating = outdoorSeating;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getReservations() {
        return reservations;
    }

    public void setReservations(String reservations) {
        this.reservations = reservations;
    }

    public String getTakeout() {
        return takeout;
    }

    public void setTakeout(String takeout) {
        this.takeout = takeout;
    }

    public String getTelevision() {
        return television;
    }

    public void setTelevision(String television) {
        this.television = television;
    }

    public String getWaiterService() {
        return waiterService;
    }

    public void setWaiterService(String waiterService) {
        this.waiterService = waiterService;
    }

    public String getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(String wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }
}
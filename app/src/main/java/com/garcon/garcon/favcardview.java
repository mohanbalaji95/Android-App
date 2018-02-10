package com.garcon.garcon;

/**
 * Created by kushaldileep on 6/21/2016.
 * Model class to display the favourite item
 */
public class favcardview {
String name;
int photoid;
    String extra;
    favcardview(String name,int photoid){
        this.name = name;
        this.photoid = photoid;
    }
    favcardview(String name, int photoid, String extra)
    {
        this.name=name;
        this.photoid=photoid;
        this.extra=extra;
    }
}


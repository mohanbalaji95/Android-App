package com.garcon.garcon;

/**
 * Created by raja on 7/21/2016.
 */
public class Category {

    private String id;
    private String name;

    public Category(String id, String name){
        this.id = id;
        this.name = name;
    }

    String getId(){
        return id;
    }

    String getName(){
        return name;
    }

}

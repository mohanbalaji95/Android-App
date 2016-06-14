package com.garcon.Models;

/**
 * Created by akshaymathur on 6/13/16.
 */
public class User {
    //name and address string
    private String email;
    private String password;

    public User() {
      /*Blank default constructor essential for Firebase*/
    }
    //Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
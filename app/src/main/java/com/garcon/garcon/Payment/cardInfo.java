package com.garcon.garcon.Payment;

/**
 * Created by kusumasri on 7/18/17.
 */

public class cardInfo {

    String number;
    String cvc2;
    int exp_month;
    int exp_year;


    public cardInfo(String cardNumber, String cvv, int s, int s1) {

        this.number=cardNumber;
        this.cvc2=cvv;
        this.exp_month=s;
        this.exp_year=s1;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvc2() {
        return cvc2;
    }

    public void setCvc2(String cvc2) {
        this.cvc2 = cvc2;
    }

    public int getExp_month() {
        return exp_month;
    }

    public void setExp_month(int exp_month) {
        this.exp_month = exp_month;
    }

    public int getExp_year() {
        return exp_year;
    }

    public void setExp_year(int exp_year) {
        this.exp_year = exp_year;
    }



}

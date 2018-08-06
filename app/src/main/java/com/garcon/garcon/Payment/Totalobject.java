package com.garcon.garcon.Payment;

import java.io.Serializable;

/**
 * Created by kusumasri on 7/14/17.
 */

public class Totalobject implements Serializable {
    int total;
    int subtotal;
    int  tax;
    int due;
    int tip;
    int serviceCharge;

    public int getTotal() {
        total = subtotal + tax + tip + serviceCharge;
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public int getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(int serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
}

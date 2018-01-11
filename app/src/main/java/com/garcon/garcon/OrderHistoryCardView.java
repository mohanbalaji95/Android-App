package com.garcon.garcon;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by siddhiparekh11 on 8/10/17.
 */

public class OrderHistoryCardView implements Serializable {

    
        String resname;
        Date orderdate;
        double totalAmount;
        String reslocation;
        String orderid;

    public OrderHistoryCardView(String resname, Date orderdate, double total, String reslocation, String orderid) {
        this.resname = resname;
        this.orderdate = orderdate;
        this.totalAmount = total;
        this.reslocation = reslocation;
        this.orderid=orderid;

    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public double getTotal() {
        return totalAmount;
    }

    public void setTotal(double total) {
        this.totalAmount = total;
    }

    public String getReslocation() {
        return reslocation;
    }

    public void setReslocation(String reslocation) {
        this.reslocation = reslocation;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}

package com.garcon.garcon;

/**
 * Created by siddhiparekh11 on 8/16/17.
 */

public class OrderHistoryView {

    double totalAmount;
    String omnivore_loc_id;
    String orderid;

    public OrderHistoryView() {

    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOmnivore_loc_id() {
        return omnivore_loc_id;
    }

    public void setOmnivore_loc_id(String omnivore_loc_id) {
        this.omnivore_loc_id = omnivore_loc_id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}

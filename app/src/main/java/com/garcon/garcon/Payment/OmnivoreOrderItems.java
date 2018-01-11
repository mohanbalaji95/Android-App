package com.garcon.garcon.Payment;

/**
 * Created by kusumasri on 7/6/17.
 */

//To store the final order items from omnivore before payment begins. Its like a bill where we can't change the items in the list.
// we can only view the items, quantity,amount to be paid. --used in checkoutandpay.java
public class OmnivoreOrderItems {

    String itemname;
    int price;
    int quantity;

    public OmnivoreOrderItems(String itemname, int price, int quantity) {
        this.itemname = itemname;
        this.price = price;
        this.quantity = quantity;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}

package com.garcon.Models;

import java.io.Serializable;

/**
 * Created by akshaymathur on 7/12/16.
 */
public class OrderMenuItem implements Serializable{

    private String menuItemID;
    private int quantity;
    private String priceLevelID;
    private String comment;
    private MenuItemModifier modifier[];

    public String getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(String menuItemID) {
        this.menuItemID = menuItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPriceLevelID() {
        return priceLevelID;
    }

    public void setPriceLevelID(String priceLevelID) {
        this.priceLevelID = priceLevelID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MenuItemModifier[] getModifier() {
        return modifier;
    }

    public void setModifier(MenuItemModifier[] modifier) {
        this.modifier = modifier;
    }
}

package com.garcon.Models;

import java.io.Serializable;

/**
 * Created by akshaymathur on 7/12/16.
 */
public class MenuItemModifier implements Serializable{
    private String menuModifierID;
    private int quantity;
    private String comment;

    public String getMenuModifierID() {
        return menuModifierID;
    }

    public void setMenuModifierID(String menuModifierID) {
        this.menuModifierID = menuModifierID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

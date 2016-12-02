package com.garcon.garcon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by raja on 7/18/2016.
 */

// class no longer used

public class OrderedItem extends MenuItem implements Serializable{

    private int numOrdered;
    private String specialInstructions;

    public OrderedItem(MenuItem m, int numOrdered, String specialInstructions){
        super(m.id,m.name,m.price,m.price_levels,m.in_stock,m.mGroups,m.modifier_groups_count,m.categoryID);
        this.numOrdered = numOrdered;
        this.specialInstructions = specialInstructions;
    }

    String getSpecialInstructions() {
        return this.specialInstructions;
    }
    void setSpecialInstructions(String s){
        this.specialInstructions = s;
    }
    int getNumOrdered(){
        return this.numOrdered;
    }
    void setNumOrdered(int amount){
        this.numOrdered = amount;
    }


    static class ItemModifier extends MenuItem.ModifierGroup.ItemModifier{

        private boolean isAdded;

        public ItemModifier(String id, String name, int price_per_unit, ArrayList<PriceLevel> priceLevelsList){
            super(id,name,price_per_unit,priceLevelsList);
        }

        public void setAdded(boolean bool){
            isAdded = bool;
        }

    }

}
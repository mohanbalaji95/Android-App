package com.garcon.garcon;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Created by raja on 7/17/2016.
 */

public class OrderSingleton {

    private static OrderSingleton mInstance = null;
    private ArrayList<MenuItem> mList;

    private OrderSingleton() {
        mList = new ArrayList<MenuItem>();
    }

    public static OrderSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new OrderSingleton();
        }
        return mInstance;
    }

    public void clearList(){
        mList = new ArrayList<MenuItem>();
    }

    public ArrayList<MenuItem> getList() {
        return this.mList;
    }

    public void addItem(MenuItem menuItem, int numOrdered, String specialInstructions) {

        MenuItem nItem = menuItem; //new MenuItem(menuItem,numOrdered,specialInstructions);
        nItem.setNumOrdered(numOrdered);
        nItem.setSpecialInstructions(specialInstructions);
        boolean added = false;

        for(MenuItem mItem : mList) {
            if (mItem.getName().equals(nItem.getName())) {
                if (mItem.getSpecialInstructions().equals(nItem.getSpecialInstructions())) {
                    if (modifiersSame(mItem, nItem)) {
                        mItem.setNumOrdered(nItem.getNumOrdered());
                        added = true;
                        break;
                    }
                    else {
                        mList.add(nItem);
                        added = true;
                        break;
                    }
                }
            }
        }

        if(!added){
            mList.add(nItem);
            added = true;
        }

    }

    private boolean modifiersSame(MenuItem a, MenuItem b){
        ArrayList<MenuItem.ModifierGroup> aList = a.getModifierGroups();
        ArrayList<MenuItem.ModifierGroup> bList = b.getModifierGroups();

        if(aList.size() == bList.size()) {
            for (int i = 0; i < aList.size(); i++) {
                MenuItem.ModifierGroup amg = aList.get(i);
                MenuItem.ModifierGroup bmg = bList.get(i);
                if (amg.getModifierList().size() == bmg.getModifierList().size()) {
                    ArrayList<MenuItem.ModifierGroup.ItemModifier> aList1 = amg.getModifierList();
                    ArrayList<MenuItem.ModifierGroup.ItemModifier> bList1 = bmg.getModifierList();
                    for (int j = 0; j < aList1.size(); j++) {
                        if(aList1.get(j).isAdded()!=bList1.get(j).isAdded()){
                            return false;
                        }
                    }
                }
                else {
                    return false;
                }
            }
        }
        else{
            return false;
        }

        return true;
    }

}
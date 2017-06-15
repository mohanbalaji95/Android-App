package com.garcon.garcon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by raja on 7/11/2016.
 */

public class MenuItem implements Serializable {
    
    protected String id;
    protected String name;
    protected int price;
    protected ArrayList<PriceLevel> price_levels;
    protected boolean in_stock;
    protected int modifier_groups_count;
    protected ArrayList<ModifierGroup> mGroups;

    //not defined in Omnivore but will give Category's ID to MenuItem
    protected String categoryID;

    //user defined variable
    private int numOrdered = 0;
    private String specialInstructions = "";

    //http://stackoverflow.com/questions/18814076/how-to-make-intellij-show-eclipse-like-api-documentation-on-mouse-hover
    /**
     * Complete constructor for MenuItem.
     *
     * @param  id The menu item’s id as stored in the POS. Sometimes a compound value derived from other data
     * @param  name The name of the Menu Item as stored in the POS
     * @param  price The price, in cents
     * @param  price_levels Array of Hashes (id String Price Level Identifier, price Integer The price of the menu item at this price level, in cents)
     * @param  in_stock Whether or not the item is currently available for order.
     * @param  mGroups Modifier Groups associated with the Menu Item.
     * @param  modifier_groups_count The number of Modifier Groups associated with the Menu Item.
     * @param  categoryID parent category's id NOT name
     */

    public MenuItem(String id, String name, Integer price, ArrayList<PriceLevel> price_levels,
                    boolean in_stock, ArrayList<ModifierGroup> mGroups, Integer modifier_groups_count, String categoryID){
        this.id = id;
        this.name = name;
        this.price = price;
        this.price_levels = price_levels;
        this.in_stock = in_stock;
        this.mGroups = mGroups;
        this.modifier_groups_count = modifier_groups_count;

        this.categoryID = categoryID;

    }

    public ArrayList<PriceLevel> getPrice_levels() {
        return price_levels;
    }

    public void setPrice_levels(ArrayList<PriceLevel> price_levels) {
        this.price_levels = price_levels;
    }

    String getId() {
        return id;
    }
    String getName(){
        return name;
    }
    String getCategoryID() {
        return categoryID;
    }
    int getPrice(){
        return this.price;
    }
    ArrayList<ModifierGroup> getModifierGroups(){ return this.mGroups;}

    int getNumOrdered(){return this.numOrdered;}
    void setNumOrdered(int amount){
        numOrdered = amount;
    }
    String getSpecialInstructions(){return this.specialInstructions;}
    void setSpecialInstructions(String instructions){
        specialInstructions = instructions;
    }

    static class ModifierGroup implements Serializable{
        private String id, name;
        private Integer minimum, maximum;
        private boolean required;
        private ArrayList<ItemModifier> modifier_list;

        public ModifierGroup(String id, String name, int minimum, int maximum, boolean required, ArrayList<ItemModifier> modifier_list){
            this.id = id;
            this.name = name;
            this.minimum = minimum;
            this.maximum = maximum;
            this.required = required;
            this.modifier_list = modifier_list;
        }

            String getId(){return id;}
            String getName(){return name;}
            Integer getMinimum(){return minimum;}
            Integer getMaximum(){return maximum;}
            boolean isRequired(){return required;}
            ArrayList<ItemModifier> getModifierList(){ return this.modifier_list;}

            static class ItemModifier implements Serializable{
                private String id, name;
                private int price_per_unit;
                private ArrayList<PriceLevel> priceLevelsList;
                //user defined variable
                private boolean added = false;

                public ItemModifier(String id, String name, int price_per_unit, ArrayList<PriceLevel> priceLevelsList){
                    this.id = id;
                    this.name = name;
                    this.price_per_unit = price_per_unit;
                    this.priceLevelsList = priceLevelsList;
                }

                String getId(){return id;}
                String getName(){return name;}
                Integer getPricePerUnit(){return price_per_unit;}
                ArrayList<PriceLevel> getPriceLevelsList(){return priceLevelsList;}
                boolean isAdded(){ return added;}
                void setAdded(boolean b){added = b;}

            }

            static class ItemModifierGrouped extends ItemModifier implements Serializable{
                private int group_id;
                public ItemModifierGrouped(String id, String name, int price_per_unit, ArrayList<PriceLevel> priceLevelsList, int group_id){
                    super(id,name,price_per_unit,priceLevelsList);
                    this.group_id = group_id;
                }
            }

    }

    static class PriceLevel implements Serializable{
        private String id;
        private Integer price;
        public PriceLevel(String id, Integer price){
            this.id = id;
            this.price = price;
        }
        String getId(){return id;}
        Integer getPrice(){return price;}
    }

}
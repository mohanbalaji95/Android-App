package com.garcon.Models;

import com.garcon.garcon.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akshaymathur on 7/12/16.
 */
public class Ticket implements Serializable{

    private String ticketID;
    private long ticketNumber;
    private long opened_at;
    private long closed_at;
    private boolean open;
    private String Employee;
    private String order_type;
    private String revenue_center;
    private String Table;
    private int guest_count;
    private String ticketName;
    private boolean auto_send;
    private String userUUID;
    private String omnivore_location_id;
    private ArrayList<MenuItem> mMenuItemArrayList;
    private String totalAmount;
    private String ccLast4Digits;

    public String getCcLast4Digits() {
        return ccLast4Digits;
    }

    public void setCcLast4Digits(String ccLast4Digits) {
        this.ccLast4Digits = ccLast4Digits;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<MenuItem> getMenuItemArrayList() {
        return mMenuItemArrayList;
    }

    public void setMenuItemArrayList(ArrayList<MenuItem> menuItemArrayList) {
        mMenuItemArrayList = menuItemArrayList;
    }

    public String getOmnivore_location_id() {
        return omnivore_location_id;
    }

    public void setOmnivore_location_id(String omnivore_location_id) {
        this.omnivore_location_id = omnivore_location_id;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public long getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public long getOpened_at() {
        return opened_at;
    }

    public void setOpened_at(long opened_at) {
        this.opened_at = opened_at;
    }

    public long getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(long closed_at) {
        this.closed_at = closed_at;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String employee) {
        Employee = employee;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getRevenue_center() {
        return revenue_center;
    }

    public void setRevenue_center(String revenue_center) {
        this.revenue_center = revenue_center;
    }

    public String getTable() {
        return Table;
    }

    public void setTable(String table) {
        Table = table;
    }

    public int getGuest_count() {
        return guest_count;
    }

    public void setGuest_count(int guest_count) {
        this.guest_count = guest_count;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public boolean isAuto_send() {
        return auto_send;
    }

    public Ticket(String ticketID, long ticketNumber, long opened_at, long closed_at, boolean open, String employee, String order_type, String revenue_center, String table, int guest_count, String ticketName, boolean auto_send, String userUUID,String omnivore_location_id) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.opened_at = opened_at;
        this.closed_at = closed_at;
        this.open = open;
        Employee = employee;
        this.order_type = order_type;
        this.revenue_center = revenue_center;
        Table = table;
        this.guest_count = guest_count;
        this.ticketName = ticketName;
        this.auto_send = auto_send;
        this.userUUID = userUUID;
        this.omnivore_location_id = omnivore_location_id;
    }

    public Ticket(String ticketID,String employee, String order_type, String revenue_center, String table, int guest_count, String ticketName, boolean auto_send,String userUUID,String omnivore_location_id) {
        this.ticketID = ticketID;
        Employee = employee;
        this.order_type = order_type;
        this.revenue_center = revenue_center;
        Table = table;
        this.guest_count = guest_count;

        this.ticketName = ticketName;
        this.auto_send = auto_send;
        this.userUUID = userUUID;
        this.omnivore_location_id = omnivore_location_id;
    }

    public void setAuto_send(boolean auto_send) {
        this.auto_send = auto_send;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", ticketID);
        result.put("ticketNumber",ticketNumber);
        result.put("opened_id",opened_at);
        result.put("closed_at",closed_at);
        result.put("open",open);
        result.put("userUUID",userUUID);
        result.put("employee", Employee);
        result.put("order_type", order_type);
        result.put("revenue_center", revenue_center);
        result.put("table", Table);
        result.put("guest_count",guest_count);
        result.put("name", ticketName);
        result.put("auto_send", auto_send);
        result.put("omnivore_loc_id", omnivore_location_id);
        result.put("menu_items",mMenuItemArrayList);

        return result;
    }
}

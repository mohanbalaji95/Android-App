package com.garcon.garcon;

import java.util.HashMap;

/**
 * Created by akshaymathur on 1/28/18.
 */

public class HolidayCalendarSingleton {

    private static HolidayCalendarSingleton sSingleton;
    private HashMap<String,String> holidayCalendar;

    private HolidayCalendarSingleton(){
        holidayCalendar = new HashMap<>();

    }

    public static HolidayCalendarSingleton getInstance(){
        if(sSingleton == null){
            sSingleton = new HolidayCalendarSingleton();
        }

        return sSingleton;
    }

    public HashMap<String,String> getHolidayCalendar(){
        return holidayCalendar;
    }

    public void setHolidayCalendar(HashMap<String,String> calendar){
        holidayCalendar = calendar;
    }
}

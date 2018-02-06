package com.garcon.garcon;

import java.util.HashMap;

/**
 * Created by akshaymathur on 1/28/18.
 * This singleton class maintains reference to the current year holiday calendar.
 * It helps in notifying user about possible changes in restaurants hours.
 * The holiday calendar is stored in HashMap(key value pairs).
 *      The keys are the dates and value is the name of the holiday.
 *      eg. July 4 would be stored as Key: "6-4" and Values: "Independence Day"
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

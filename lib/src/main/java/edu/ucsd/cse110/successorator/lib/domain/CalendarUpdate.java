package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;
import java.util.Date;

public class CalendarUpdate {

     private static Calendar cal;

     public static void initializeCal() {
         long currentMillis = System.currentTimeMillis();
         Date date = new Date(currentMillis);
         cal = Calendar.getInstance();
         cal.setTime(date);
     }

    public static void incrementDateBy1(){
        cal.add(Calendar.DATE, 1);
    }

    public static Calendar getCal(){
        return cal;
    }

    public static void resetCal(){
        cal = Calendar.getInstance();
    }



}

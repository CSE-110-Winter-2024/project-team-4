package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;

public class CalendarUpdate {

     private static Calendar cal = Calendar.getInstance();

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

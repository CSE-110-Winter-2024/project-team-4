package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;

public class CalendarUpdate {


    public Calendar incrementDateBy1(Calendar cal){
        cal.add(Calendar.DATE, 1);
        return cal;
    }



    /*
     private Calendar cal;

    public CalendarUpdate(){
        this.cal = Calendar.getInstance();
    }

    public void incrementDateBy1(){
        cal.add(Calendar.DATE, 1);
    }

    public Calendar getCal(){
        return cal;
    }
     */
}

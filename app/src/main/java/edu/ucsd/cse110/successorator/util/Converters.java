package edu.ucsd.cse110.successorator.util;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class Converters {

    @TypeConverter
    public static Calendar toCalendar(long mil) {
        if (mil == 0) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mil);
        return cal;
    }

    @TypeConverter
    public static long fromCalendar(Calendar cal) {
        if (cal == null) return 0;
        return cal.getTimeInMillis();
    }

}

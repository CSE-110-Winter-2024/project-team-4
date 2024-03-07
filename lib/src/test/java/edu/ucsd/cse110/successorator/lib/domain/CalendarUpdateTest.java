package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;

public class CalendarUpdateTest {

    @Test
    public void incrementDateBy1() {

        CalendarUpdate.resetCal();
        CalendarUpdate.incrementDateBy1();
        Calendar cal = CalendarUpdate.getCal();
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, 1);
        var dateFormat2 = DateFormat.getDateInstance(DateFormat.FULL).format(cal2.getTime());
        assertEquals(dateFormat2.toString(), dateFormat.toString());
    }
}
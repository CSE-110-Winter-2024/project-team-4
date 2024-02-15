//package edu.ucsd.cse110.successorator;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import java.text.DateFormat;
//import java.util.Calendar;
//
//public class MainActivityTest {
//
//    @Test
//    public void incrementCurrentDate() {
//        Calendar cal = Calendar.getInstance();
//        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
//
//
//        MainActivity mainActivity = new MainActivity();
//        mainActivity.incrementCurrentDate();
//        assertEquals("2021-04-02", mainActivity.getCurrentDate());
//    }
//}
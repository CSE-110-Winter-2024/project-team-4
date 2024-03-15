package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;

public class TaskTest {

    @Test
    public void id() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        assertEquals(Integer.valueOf(1), task.id());
    }

    @Test
    public void name() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        assertEquals("name", task.name());
    }

    @Test
    public void complete() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        assertEquals(false, task.complete());
    }

    @Test
    public void type(){
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        assertEquals("Today", task.type());
    }

    @Test
    public void sortOrder() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        assertEquals(1, task.sortOrder());
    }

    @Test
    public void context() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        assertEquals("H", task.context());
    }

    @Test
    public void recurringInterval() {
        Task task = new Task(1, "name", false, 1, "date", "Today", -1, null, true, null, true, null, false,"H");
        assertEquals(-1, task.recurringInterval());

        Task task2 = new Task(1, "name", false, 1, "date", "Today", 10000, null, true, null, true, null, false,"H");
        assertEquals(10000, task2.recurringInterval());

        task.setRecurringInterval(1234);
        assertEquals(1234, task.recurringInterval());

        task2.setRecurringInterval(365);
        assertEquals(365, task2.recurringInterval());
    }

    @Test
    public void startDate() {
        Task task = new Task(1, "name", false, 1, "date", "Today", -1, null, true, null, true, null, false, "H");
        assertEquals(null, task.startDate());

        Calendar mockStartDate = Calendar.getInstance();
        mockStartDate.set(2024, Calendar.MARCH, 14); // Example start date
        Calendar startDate = (Calendar) mockStartDate.clone();
        startDate.set(2024, Calendar.MARCH, 14); // Example start date
        Task task2 = new Task(1, "name", false, 1, "date", "Today", -1, startDate, true, null, true, null, false, "H");
        assertEquals(mockStartDate, task2.startDate());
    }

    @Test
    public void onDisplay() {
        Task task = new Task(1, "name", false, 1, "date", "Today", -1, null, true, null, true, null, false,"H");
        assertEquals(true, task.onDisplay());
        Task task2 = new Task(1, "name", false, 1, "date", "Today", 10000, null, false, null, true, null, false,"H");
        assertEquals(false, task2.onDisplay());

        task.setOnDisplay(false);
        assertEquals(false, task.onDisplay());
        task2.setOnDisplay(true);
        assertEquals(true, task2.onDisplay());
    }

    @Test
    public void nextDate() {
        Task task = new Task(1, "name", false, 1, "date", "Today", -1, null, true, null, true, null, false, "H");
        assertEquals(null, task.nextDate());

        Calendar mockNextDate = Calendar.getInstance();
        mockNextDate.set(2024, Calendar.SEPTEMBER, 14); // Example start date
        Calendar nextDate = (Calendar) mockNextDate.clone();
        nextDate.set(2024, Calendar.SEPTEMBER, 14); // Example start date
        Task task2 = new Task(1, "name", false, 1, "date", "Today", -1, null, true, nextDate, true, null, false, "H");
        assertEquals(mockNextDate, task2.nextDate());
    }




    @Test
    public void withId() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task expected = new Task(42, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task actual = task.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void withType(){
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task expected = new Task(1, "name", false, 1, "date", "Tomorrow", 0, null, true, null, true, null, false,"H");
        Task actual = task.withType("Tomorrow");
        assertEquals(expected, actual);
    }

    @Test
    public void withSortOrder() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task expected = new Task(1, "name", false, 42, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task actual = task.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Task task1 = new Task(1, "name", false, 0, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task task2 = new Task(1, "name", false, 0, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task task3 = new Task(2, "name", false, 0, "date", "Today", 0, null, true, null, true, null, false,"H");

        assertEquals(task1, task2);
        assertNotEquals(task2, task3);
    }

    @Test
    public void setComplete(){
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        Task expected = new Task(1, "name", true, 1, "date", "Today", 0, null, true, null, true, null, false,"H");
        task.setComplete(true);
        assertEquals(expected, task);
    }

    @Test
    public void setType(){
        Task task = new Task(1, "name", false, 1, "date", "Pending", 0, null, true, null, true, null, false,"H");
        Task expected = new Task(1, "name", false, 1, "date", "Tomorrow", 0, null, true, null, true, null, false,"H");
        task.setType("Tomorrow");
        assertEquals(expected, task);
    }



}

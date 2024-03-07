package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

    @Test
    public void id() {
        Task task = new Task(1, "name", false, 1, "date", "Today");
        assertEquals(Integer.valueOf(1), task.id());
    }

    @Test
    public void name() {
        Task task = new Task(1, "name", false, 1, "date", "Today");
        assertEquals("name", task.name());
    }

    @Test
    public void complete() {
        Task task = new Task(1, "name", false, 1, "date", "Today");
        assertEquals(false, task.complete());
    }

    @Test
    public void type(){
        Task task = new Task(1, "name", false, 1, "date", "Today");
        assertEquals("Today", task.type());
    }

    @Test
    public void sortOrder() {
        Task task = new Task(1, "name", false, 1, "date", "Today");
        assertEquals(1, task.sortOrder());
    }

    @Test
    public void withId() {
        Task task = new Task(1, "name", false, 1, "date", "Today");
        Task expected = new Task(42, "name", false, 1, "date", "Today");
        Task actual = task.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void withType(){
        Task task = new Task(1, "name", false, 1, "date", "Today");
        Task expected = new Task(1, "name", false, 1, "date", "Tomorrow");
        Task actual = task.withType("Tomorrow");
        assertEquals(expected, actual);
    }

    @Test
    public void withSortOrder() {
        Task task = new Task(1, "name", false, 1, "date", "Today");
        Task expected = new Task(1, "name", false, 42, "date", "Today");
        Task actual = task.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Task task1 = new Task(1, "name", false, 0, "date", "Today");
        Task task2 = new Task(1, "name", false, 0, "date", "Today");
        Task task3 = new Task(2, "name", false, 0, "date", "Today");

        assertEquals(task1, task2);
        assertNotEquals(task2, task3);
    }

}
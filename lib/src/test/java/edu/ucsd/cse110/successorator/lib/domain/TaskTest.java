package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

    @Test
    public void id() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        assertEquals(Integer.valueOf(1), task.id());
    }

    @Test
    public void name() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        assertEquals("name", task.name());
    }

    @Test
    public void complete() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        assertEquals(false, task.complete());
    }

    @Test
    public void type(){
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        assertEquals("Today", task.type());
    }

    @Test
    public void sortOrder() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        assertEquals(1, task.sortOrder());
    }

    @Test
    public void context() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        assertEquals("H", task.context());
    }

    @Test
    public void withId() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task expected = new Task(42, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task actual = task.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void withType(){
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task expected = new Task(1, "name", false, 1, "date", "Tomorrow", 0, null, true, null, true, null, "H");
        Task actual = task.withType("Tomorrow");
        assertEquals(expected, actual);
    }

    @Test
    public void withSortOrder() {
        Task task = new Task(1, "name", false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task expected = new Task(1, "name", false, 42, "date", "Today", 0, null, true, null, true, null, "H");
        Task actual = task.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Task task1 = new Task(1, "name", false, 0, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(1, "name", false, 0, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(2, "name", false, 0, "date", "Today", 0, null, true, null, true, null, "H");

        assertEquals(task1, task2);
        assertNotEquals(task2, task3);
    }

}
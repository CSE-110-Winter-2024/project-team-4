package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.Nullable;

public class Task implements Serializable {

    private final @Nullable Integer id;
    private final @NonNull String name;
    private boolean complete;
    private int sortOrder;
    private String date;
    private String type;
    private final long recurringInterval;
    private final long startDate;




    public Task(@Nullable Integer id, @NonNull String name, boolean complete, int sortOrder, String date, @NonNull String type, long recurringInterval, long startDate)
    {
        this.id = id;
        this.name = name;
        this.complete = complete;
        this.sortOrder = sortOrder;
        this.date = date;
        this.type = type;
        this.recurringInterval = recurringInterval;
        this.startDate = startDate;
    }

    public void setComplete(boolean val)
    {
        this.complete = val;
    }

    public void setType(String type){
        this.type = type;
    }

    public @Nullable Integer id()
    {
        return id;
    }

    public @NonNull String name()
    {
        return name;
    }

    public boolean complete()
    {
        return complete;
    }

    public int sortOrder()
    {
        return sortOrder;
    }

    public @NonNull String type(){return this.type;}

    public long recurringInterval() { return recurringInterval;}

    public long startDate() { return startDate;}

    public Task withId(int id)
    {
        Task newTask = new Task(id, this.name, this.complete, this.sortOrder, this.date, this.type, this.recurringInterval, this.startDate);
        return newTask;
    }

    public Task withSortOrder(int sortOrder) {
        Task newTask =  new Task(id, this.name, this.complete, sortOrder, this.date, this.type, this.recurringInterval, this.startDate);
        return newTask;
    }

    public Task withType(String type){
        Task newTask = new Task(id, this.name, this.complete, this.sortOrder, this.date, type, this.recurringInterval, this.startDate);
        return newTask;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return complete == task.complete && sortOrder == task.sortOrder && Objects.equals(id, task.id)
                && Objects.equals(name, task.name) && Objects.equals(date, task.date) && Objects.equals(type, task.type)
                && Objects.equals(recurringInterval, task.recurringInterval) && Objects.equals(startDate, task.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, complete, sortOrder, date, type, recurringInterval, startDate);
    }
}

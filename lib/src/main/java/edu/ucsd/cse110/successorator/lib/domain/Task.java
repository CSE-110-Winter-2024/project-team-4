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

    public Task(@Nullable Integer id, @NonNull String name, boolean complete, int sortOrder, String date)
    {
        this.id = id;
        this.name = name;
        this.complete = complete;
        this.sortOrder = sortOrder;
        this.date = date;
    }

    public void setComplete(boolean val)
    {
        this.complete = val;
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

    public Task withId(int id)
    {
        Task newTask = new Task(id, this.name, this.complete, this.sortOrder, this.date);
        return newTask;
    }

    public Task withSortOrder(int sortOrder) {
        Task newTask =  new Task(id, this.name, this.complete, sortOrder, this.date);
        return newTask;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return complete == task.complete && sortOrder == task.sortOrder && Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(date, task.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, complete, sortOrder, date);
    }
}

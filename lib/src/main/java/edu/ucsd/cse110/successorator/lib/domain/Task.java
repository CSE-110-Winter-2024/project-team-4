package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.Nullable;

public class Task implements Serializable {

    private final @Nullable Integer id;
    private final @NonNull String name;
    private boolean complete;

    private int sortOrder;

    public Task(@Nullable Integer id, @NonNull String name, boolean complete, int sortOrder)
    {
        this.id = id;
        this.name = name;
        this.complete = complete;
        this.sortOrder = sortOrder;
    }

    public void setComplete(boolean val)
    {
        System.out.println("TASK.JAVA SETCOMPLETE");
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
        Task newTask = new Task(id, this.name, this.complete, this.sortOrder);
        return newTask;
    }

    public Task withSortOrder(int sortOrder) {
        Task newTask =  new Task(id, this.name, this.complete, sortOrder);
        return newTask;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return sortOrder == task.sortOrder && complete == task.complete && Objects.equals(id, task.id) && Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, complete, sortOrder);
    }
}

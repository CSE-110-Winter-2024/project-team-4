package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.Nullable;

public class Task implements Serializable {

    private final @Nullable Integer id;
    private @NonNull String name;
    private boolean complete;
    private int sortOrder;
    private String date;
    private String type;
    private long recurringInterval;
    private Calendar startDate;
    private boolean onDisplay;
    private Calendar nextDate;
    private boolean createdNextRecurring;
    private Calendar completedDate;
    private boolean isFifthWeekOfMonth;

//    private String recurrenceType;




    public Task(@Nullable Integer id, @NonNull String name, boolean complete, int sortOrder, String date,
                @NonNull String type, long recurringInterval, Calendar startDate, boolean onDisplay,
                Calendar nextDate, boolean createdNextRecurring, Calendar completedDate, boolean isFifthWeekOfMonth)
    {
        this.id = id;
        this.name = name;
        this.complete = complete;
        this.sortOrder = sortOrder;
        this.date = date;
        this.type = type;
        this.recurringInterval = recurringInterval;
        this.startDate = startDate;
        this.onDisplay = onDisplay;
        this.nextDate = nextDate;
        this.createdNextRecurring = createdNextRecurring;
        this.completedDate = completedDate;
        this.isFifthWeekOfMonth = isFifthWeekOfMonth;
//        this.recurrenceType = recurrenceType;
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
    public void setName(String name) { this.name = name;}


    public boolean complete()
    {
        return complete;
    }

    public int sortOrder()
    {
        return sortOrder;
    }
    public String date()
    {
        return date;
    }

    public @NonNull String type(){return this.type;}

    public long recurringInterval() { return recurringInterval;}
    public void setRecurringInterval(long recurringInterval) { this.recurringInterval = recurringInterval;}

    public Calendar startDate() { return startDate;}

    public boolean onDisplay() { return onDisplay; }
    public void setOnDisplay(boolean onDisplay) { this.onDisplay = onDisplay; }
    public Calendar nextDate() { return nextDate; }
    public void setNextDate(Calendar nextDate) { this.nextDate = nextDate; }
    public boolean createdNextRecurring() { return createdNextRecurring; }
    public void setCreatedNextRecurring(boolean createdNextRecurring) { this.createdNextRecurring = createdNextRecurring; }
    public Calendar completedDate() { return completedDate; }
    public void setCompletedDate(Calendar completedDate) { this.completedDate = completedDate; }
//    public String recurrenceType() { return recurrenceType; }
//    public void setRecurrenceType(String recurrenceType) { this.recurrenceType = recurrenceType; }

    public boolean isFifthWeekOfMonth() { return isFifthWeekOfMonth; }
    public void setIsFifthWeekOfMonth(boolean isFifthWeekOfMonth) { this.isFifthWeekOfMonth = isFifthWeekOfMonth; }

    public Task withId(int id)
    {
        Task newTask = new Task(id, this.name, this.complete, this.sortOrder, this.date, this.type,
                this.recurringInterval, this.startDate, this.onDisplay, this.nextDate,
                this.createdNextRecurring, this.completedDate, this.isFifthWeekOfMonth);
        return newTask;
    }

    public Task withSortOrder(int sortOrder) {
        Task newTask =  new Task(id, this.name, this.complete, sortOrder, this.date, this.type,
                this.recurringInterval, this.startDate, this.onDisplay, this.nextDate,
                this.createdNextRecurring, this.completedDate, this.isFifthWeekOfMonth);
        return newTask;
    }

    public Task withType(String type){
        Task newTask = new Task(id, this.name, this.complete, this.sortOrder, this.date, type,
                this.recurringInterval, this.startDate, this.onDisplay, this.nextDate,
                this.createdNextRecurring, this.completedDate, this.isFifthWeekOfMonth);
        return newTask;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return complete == task.complete && sortOrder == task.sortOrder && Objects.equals(id, task.id)
                && Objects.equals(name, task.name) && Objects.equals(date, task.date) && Objects.equals(type, task.type)
                && Objects.equals(recurringInterval, task.recurringInterval) && Objects.equals(startDate, task.startDate)
                && onDisplay == task.onDisplay && Objects.equals(nextDate, task.nextDate)
                && createdNextRecurring == task.createdNextRecurring && Objects.equals(completedDate, task.completedDate)
                && isFifthWeekOfMonth == task.isFifthWeekOfMonth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, complete, sortOrder, date, type, recurringInterval, startDate,
                onDisplay, nextDate, createdNextRecurring, completedDate, isFifthWeekOfMonth);
    }
}

package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.domain.Task;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "complete")
    public boolean complete;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "recurring_interval")
    public long recurringInterval; // Interval in milliseconds

    @ColumnInfo(name = "start_date")
    public long startDate; // Interval in milliseconds


    TaskEntity(@NonNull String name, @NonNull boolean complete, @NonNull int sortOrder, @NonNull String type, long recurringInterval, long startDate){
        this.name = name;
        this.complete = complete;
        this.sortOrder = sortOrder;
        this.type = type;
        this.recurringInterval = recurringInterval;
        this.startDate = startDate;
    }

    public static TaskEntity fromTask(@NonNull Task task){
        var taskEntity = new TaskEntity(task.name(), task.complete(), task.sortOrder(), task.type(), task.recurringInterval(), task.startDate());
        taskEntity.id = task.id();
        return taskEntity;
    }

    public @NonNull Task toTask(){
        Calendar cal = Calendar.getInstance();
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        date = dateFormat.toString();
        return new Task(id, name, complete, sortOrder, date, type, recurringInterval, startDate);
    }


}

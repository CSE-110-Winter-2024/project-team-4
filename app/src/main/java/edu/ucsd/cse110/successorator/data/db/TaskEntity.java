package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.DateFormat;
import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.util.Converters;

@Entity(tableName = "tasks")
@TypeConverters(Converters.class)
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
    public long recurringInterval;

    @ColumnInfo(name = "start_date")
    public Calendar startDate;

    @ColumnInfo(name = "on_display")
    public boolean onDisplay;

    @ColumnInfo(name = "next_date")
    public Calendar nextDate;

    @ColumnInfo(name = "created_next_recurring")
    public boolean createdNextRecurring;

    @ColumnInfo(name = "completed_date")
    public Calendar completedDate;

    @ColumnInfo(name = "is_fifth_week_of_month")
    public boolean isFifthWeekOfMonth;

    @ColumnInfo(name = "context")
    public String context;


    TaskEntity(@NonNull String name, @NonNull boolean complete, @NonNull int sortOrder,
               @NonNull String type, long recurringInterval, Calendar startDate, boolean onDisplay,
               Calendar nextDate, boolean createdNextRecurring, Calendar completedDate, boolean isFifthWeekOfMonth, String context){
        this.name = name;
        this.complete = complete;
        this.sortOrder = sortOrder;
        this.type = type;
        this.recurringInterval = recurringInterval;
        this.startDate = startDate;
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        this.onDisplay = onDisplay;
        this.nextDate = nextDate;
        this.createdNextRecurring = createdNextRecurring;
        this.completedDate = completedDate;
        this.isFifthWeekOfMonth = isFifthWeekOfMonth;
        this.context = context;
    }

    public static TaskEntity fromTask(@NonNull Task task){
        var taskEntity = new TaskEntity(task.name(), task.complete(), task.sortOrder(), task.type(),
                task.recurringInterval(), task.startDate(), task.onDisplay(), task.nextDate(),
                task.createdNextRecurring(), task.completedDate(), task.isFifthWeekOfMonth(), task.context());
        taskEntity.id = task.id();
        System.out.println("task entity task.id():" + task.id());
        System.out.println("task entity taskentity .id:" + taskEntity.id);
        return taskEntity;
    }

    public @NonNull Task toTask(){
        Calendar cal = Calendar.getInstance();
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        date = dateFormat.toString();
        return new Task(id, name, complete, sortOrder, date, type, recurringInterval, startDate,
                onDisplay, nextDate, createdNextRecurring, completedDate, isFifthWeekOfMonth, context);
    }


}

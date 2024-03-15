package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Calendar;
import java.util.List;

@Dao
public interface TaskDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity taskEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<TaskEntity> taskEntities);


    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity find(int id);

    @Query("SELECT * FROM tasks ORDER BY complete, sort_order")
    List<TaskEntity> findAll();

    @Query("SELECT * FROM tasks WHERE id = :id and on_display == true")
    LiveData<TaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM tasks WHERE on_display == true ORDER BY complete, sort_order")
    LiveData<List<TaskEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM tasks")
    int count();

    @Query("SELECT MIN(sort_order) FROM tasks")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM tasks")
    int getMaxSortOrder();


    @Query("UPDATE tasks SET sort_order = sort_order + :by " +
            "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Query("UPDATE tasks SET complete = :status " +
            "WHERE id = :id")
    void setComplete(int id, boolean status);

    @Query("UPDATE tasks SET type = :type " +
            "WHERE id = :id")
    void setType(int id, String type);

    @Query("UPDATE tasks SET created_next_recurring = :status " +
            "WHERE id = :id ")
    void setCreatedNextRecurring(int id, boolean status);

    @Query("UPDATE tasks SET on_display = :status " +
            "WHERE id = :id ")
    void setOnDisplay(int id, boolean status);

    @Query("UPDATE tasks SET completed_date = :status " +
            "WHERE id = :id ")
    void setCompletedDate(int id, Calendar status);

    @Query("SELECT * FROM tasks WHERE ((start_date = :status))")
    LiveData<List<TaskEntity>> getTomorrowTasks(long status);

    @Query("SELECT * FROM tasks WHERE (type = 'Today' or (start_date = :status)) and on_display == true")
    LiveData<List<TaskEntity>> getTodayTasks(long status);

    @Query("SELECT * FROM tasks WHERE (type = 'Recurring' or recurring_interval >= 0) and on_display == true")
    LiveData<List<TaskEntity>> getRecurringTasks();

    @Query("SELECT * FROM tasks WHERE (type = 'Pending') and on_display == true")
    LiveData<List<TaskEntity>> getPendingTasks();

    //@Query("SELECT * FROM tasks WHERE type = :type and (:context = '' OR CONTEXT = :context) and on_display == true")
    @Query("SELECT * FROM tasks WHERE (type = 'Today' or (start_date = :status)) and on_display == true and (:context = '' OR CONTEXT = :context) and on_display == true ORDER BY complete, CASE CONTEXT WHEN 'H' THEN 1 WHEN 'W' THEN 2 WHEN 'S' THEN 3 WHEN 'E' THEN 4 END")
    LiveData<List<TaskEntity>> getTasksByTodayAndContext(long status, String context);

    @Query("SELECT * FROM tasks WHERE (start_date = :status) and (:context = '' OR CONTEXT = :context)  ORDER BY complete, CASE CONTEXT WHEN 'H' THEN 1 WHEN 'W' THEN 2 WHEN 'S' THEN 3 WHEN 'E' THEN 4 END")
    LiveData<List<TaskEntity>> getTasksByTomorrowAndContext(long status, String context);

    @Query("SELECT * FROM tasks WHERE (type = 'Recurring' or recurring_interval >= 0) and (:context = '' OR CONTEXT = :context) and on_display == true ORDER BY complete, CASE CONTEXT WHEN 'H' THEN 1 WHEN 'W' THEN 2 WHEN 'S' THEN 3 WHEN 'E' THEN 4 END")
    LiveData<List<TaskEntity>> getTasksByRecurringAndContext(String context);
  
    @Query("SELECT * FROM tasks WHERE type = 'Pending'  and (:context = '' OR CONTEXT = :context) ORDER BY complete, CASE CONTEXT WHEN 'H' THEN 1 WHEN 'W' THEN 2 WHEN 'S' THEN 3 WHEN 'E' THEN 4 END")
    LiveData<List<TaskEntity>> getTasksByPendingAndContext(String context);

    @Transaction
    default int append(TaskEntity taskEntity){
        var maxSortOrder = getMaxSortOrder();
        var newTask = new TaskEntity(
                taskEntity.name,
                taskEntity.complete,
                maxSortOrder + 1,
                taskEntity.type,
                taskEntity.recurringInterval,
                taskEntity.startDate,
                taskEntity.onDisplay,
                taskEntity.nextDate,
                taskEntity.createdNextRecurring,
                taskEntity.completedDate,
                taskEntity.isFifthWeekOfMonth,
                taskEntity.context
        );
        System.out.println("TaskDao append newTask id: " + newTask.id);
        newTask.id = taskEntity.id;
        Integer result = Math.toIntExact(insert(newTask));
        System.out.println("TaskDao append result = " + result);
        return result;
    }

    @Transaction
    default int prepend(TaskEntity taskEntity){
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newTask = new TaskEntity(
                taskEntity.name,
                taskEntity.complete,
                getMinSortOrder() - 1,
                taskEntity.type,
                taskEntity.recurringInterval,
                taskEntity.startDate,
                taskEntity.onDisplay,
                taskEntity.nextDate,
                taskEntity.createdNextRecurring,
                taskEntity.completedDate,
                taskEntity.isFifthWeekOfMonth,
                taskEntity.context
        );
        newTask.id = taskEntity.id;
        System.out.println("TaskDao prepend argument id: " + taskEntity.id);
        System.out.println("TaskDao prepend newTask id: " + newTask.id);
        return Math.toIntExact(insert(newTask));
    }

    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM tasks WHERE complete = true AND type = 'Today' AND recurring_interval=-1")
    void deleteCompleted();

    @Query("UPDATE tasks SET type = 'Today' WHERE type = 'Tomorrow'")
    void moveTomorrowTasksToToday();

    @Query("SELECT * FROM tasks ORDER BY complete, CASE CONTEXT WHEN 'H' THEN 1 WHEN 'W' THEN 2 WHEN 'S' THEN 3 WHEN 'E' THEN 4 END")
    LiveData<List<TaskEntity>> createSortedTasksView();
}

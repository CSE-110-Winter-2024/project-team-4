package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {
    Subject<Task> find(int id);

    Subject<List<Task>> findAll();

    void save(Task task);

    void save(List<Task> tasks);

    void remove(int id);

    void append(Task task);

    void prepend(Task task);

    void setComplete(int id, boolean status);

    void setType(int id, String type);

    void removeCompleted();

    Subject<List<Task>>  filterTomorrowTasks();

    Subject<List<Task>> filterTodayTasks();

    Subject<List<Task>> filterRecurringTasks();

    void generateNextRecurringTasks();

    void setOnDisplays();

    void setTaskCompletedDate(Task task);

    void generateDailyRecurringTask(Task task);
}

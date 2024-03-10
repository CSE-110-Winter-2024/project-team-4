package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomTaskRepository implements TaskRepository {
    private final TaskDao taskDao;

    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Subject<Task> find(int id) {
        LiveData<TaskEntity> entityLiveData = taskDao.findAsLiveData(id);
        LiveData<Task> taskLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<List<Task>> findAll(){
        System.out.println("room task repo find all");
        var entitiesLiveData = taskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public void save(Task task){
        taskDao.insert(TaskEntity.fromTask(task));
    }

    @Override
    public void save(List<Task> tasks){
        var entities = tasks.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    @Override
    public void append(Task task){
        taskDao.append(TaskEntity.fromTask(task));
    }

    @Override
    public void prepend(Task task){
        taskDao.prepend(TaskEntity.fromTask(task));
    }

    @Override
    public void remove(int id){
        taskDao.delete(id);
    }

    @Override
    public void setComplete(int id, boolean status) {taskDao.setComplete(id, status);}

    @Override
    public void setType(int id, String type) {taskDao.setType(id, type);}


    public void removeCompleted(){
        taskDao.deleteCompleted();
        taskDao.moveTomorrowTasksToToday();
    }

    public Subject<List<Task>> filterTomorrowTasks(){
        var entitiesLiveData = taskDao.getTomorrowTasks();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    public Subject<List<Task>> filterTodayTasks(){
        var entitiesLiveData = taskDao.getTodayTasks();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);

    }


    public void t(Task originalTask) {
        long interval = originalTask.recurringInterval();
        long currentTime = System.currentTimeMillis();
        long nextTaskDate = originalTask.startDate() + interval;

        if (nextTaskDate <= currentTime + TimeUnit.DAYS.toMillis(2)) {
            // Create a new instance of the task with the adjusted due date
            TaskEntity recurringTask = new TaskEntity(
                    originalTask.name(),
                    originalTask.complete(),
                    originalTask.sortOrder(),
                    originalTask.type(),
                    originalTask.recurringInterval(),
                    nextTaskDate
            );

            // Store the recurring task in the database
            taskDao.append(recurringTask);

            // Calculate the due date for the next recurring task
//            nextDueDate += interval;
        }
    }



}

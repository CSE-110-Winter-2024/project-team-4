package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.CalendarUpdate;
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
    
    public void setTaskCompletedDate(Task task) {
        Calendar cal = CalendarUpdate.getCalMidnight();
        task.setCompletedDate(cal);
        taskDao.setCompletedDate(task.id(), task.completedDate());
    }
    

    public void setOnDisplays() {
        System.out.println("RoomTaskRepository setOnDisplays");
        
        if (taskDao.findAll() != null) {
            for (TaskEntity taskEntity: taskDao.findAll()) {
                Task task = taskEntity.toTask();
                int intervalDays = (int)TimeUnit.MILLISECONDS.toDays(task.recurringInterval());
                Calendar cal = CalendarUpdate.getCal();
                Calendar startTaskDate = task.startDate();
                Calendar completedTaskDate = task.completedDate();

                Calendar todayDate = CalendarUpdate.getCalMidnight();

                // set onDisplay to true if it should appear
                if (intervalDays > 0 && cal.getTimeInMillis() >= startTaskDate.getTimeInMillis()
                    && (completedTaskDate == null || completedTaskDate == todayDate)) {
                    task.setOnDisplay(true);
                    taskDao.setOnDisplay(task.id(), task.onDisplay());
                }

                // set onDisplay to false if it should not appear
                if (intervalDays > 0 && completedTaskDate.getTimeInMillis() < todayDate.getTimeInMillis()) {
                    task.setOnDisplay(false);
                    taskDao.setOnDisplay(task.id(), task.onDisplay());
                }
            }
        }
    }



    public void generateNextRecurringTasks() {
        System.out.println("RoomTaskRepository generateNextRecurringTasks");
        System.out.println(taskDao.findAll());

        if (taskDao.findAll() != null) {
            for (TaskEntity taskEntity: taskDao.findAll()) {
                Task task = taskEntity.toTask();
                System.out.println("task name: " + task.name());

                int intervalDays = (int)TimeUnit.MILLISECONDS.toDays(task.recurringInterval());
                Calendar cal = CalendarUpdate.getCal();

                Calendar startTaskDate = task.startDate();
                Calendar nextTaskDate = task.nextDate();
                System.out.println("intervalDays =  " + intervalDays);
                System.out.println("task.createdNextRecurring = " + task.createdNextRecurring());
                System.out.println(nextTaskDate.getTimeInMillis() + " <=? " + Calendar.getInstance().getTimeInMillis() + " + " + TimeUnit.DAYS.toMillis(2));

                if (intervalDays > 0 && !task.createdNextRecurring()
                        && nextTaskDate.getTimeInMillis() <= cal.getTimeInMillis() + TimeUnit.DAYS.toMillis(2)) {
                    System.out.println("----- INSIDE IF -----");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println("id = " + task.id() + ". startDate = " + dateFormat.format(task.startDate().getTime()) + ". nextDate = " + dateFormat.format(task.nextDate().getTime()));

                    // Create a new instance of the task
                    Task recurringTask = new Task (
                            null, // id
                            task.name(), // name
                            false, // complete
                            task.sortOrder(), // sort order
                            task.date(), // date
                            task.type(), // type
                            task.recurringInterval(), // recInterval
                            nextTaskDate, // startDate
                            false, // onDisplay
                            null, // nextDate
                            false,  // createdNextRecurring
                            null // completedDate
                            );

                    Calendar newNextTaskDate = (Calendar) recurringTask.startDate().clone();
                    switch(intervalDays) {
                        case 1:
                            newNextTaskDate.add(Calendar.DATE, 1);
                            break;
                        case 7:
                            newNextTaskDate.add(Calendar.WEEK_OF_YEAR, 1);
                            break;
                        case 30:
                            newNextTaskDate.add(Calendar.WEEK_OF_YEAR, 4); // TODO: FIX
                            break;
                        case 365:
                            newNextTaskDate.add(Calendar.YEAR, 1); // TODO: FIX
                            break;
                        default:
                            System.out.println("Unknown recurrence.");
                    }
                    recurringTask.setNextDate(newNextTaskDate);

                    TaskEntity recurringTaskEntity = TaskEntity.fromTask(recurringTask);
                    taskDao.append(recurringTaskEntity);

                    task.setCreatedNextRecurring(true);
                    taskDao.setCreatedNextRecurring(task.id(), task.createdNextRecurring());

            }
        }


        }
    }



}

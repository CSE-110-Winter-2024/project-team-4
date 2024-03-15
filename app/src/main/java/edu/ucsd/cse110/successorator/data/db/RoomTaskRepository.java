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
        System.out.println("RoomTaskRepo append");
        System.out.println(taskDao.append(TaskEntity.fromTask(task)));
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

    public void setCreatedNextRecurring(int id, boolean status) { taskDao.setCreatedNextRecurring(id, status);}


    public void removeCompleted(){
        taskDao.deleteCompleted();
        taskDao.moveTomorrowTasksToToday();
    }

    public Subject<List<Task>> filterTomorrowTasks(){
        Calendar cal = CalendarUpdate.getCalMidnight();
        Calendar tmrw = (Calendar)cal.clone();
        tmrw.add(Calendar.DATE,1);
        System.out.println("Gettomorrowtasks tmrw cal" + tmrw.getTimeInMillis());
        var entitiesLiveData = taskDao.getTomorrowTasks(tmrw.getTimeInMillis());
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    public Subject<List<Task>> filterTodayTasks(){
        Calendar cal = CalendarUpdate.getCalMidnight();
//        System.out.println("FILTER TIME: " + cal.getTimeInMillis());
        System.out.println("gettodaytasks cal " + cal.getTimeInMillis());
        var entitiesLiveData = taskDao.getTodayTasks(cal.getTimeInMillis());
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);

    }


    public Subject<List<Task>> filterTasksByTypeAndContext(String type, String context){
        var entitiesLiveData = taskDao.getTasksByTypeAndContext(type, context);
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }
    public Subject<List<Task>> filterPendingTasks(){
        var entitiesLiveData = taskDao.getPendingTasks();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    public Subject<List<Task>> filterRecurringTasks(){
        var entitiesLiveData = taskDao.getRecurringTasks();
        System.out.println("ENTITIES LIVE DATA: " + entitiesLiveData);
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);

    }

    public Subject<List<Task>> sortTasksByContext(){
        var entitiesLiveData = taskDao.createSortedTasksView();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }
    
    public void setTaskCompletedDate(Integer id, Task task) {
        if (task == null) {
            taskDao.setCompletedDate(id, null);
        } else {
            Calendar cal = CalendarUpdate.getCalMidnight();
            task.setCompletedDate(cal);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("state completedTaskDate:" + dateFormat.format(task.completedDate().getTime()));
            System.out.println("RoomTaskRepository taskid: " + task.id());
            taskDao.setCompletedDate(id, task.completedDate());
        }
    }
    

    public void setOnDisplays() {
        System.out.println("RoomTaskRepository setOnDisplays");
        
        if (taskDao.findAll() != null) {
            for (TaskEntity taskEntity: taskDao.findAll()) {
                Task task = taskEntity.toTask();
                int intervalDays = (int)TimeUnit.MILLISECONDS.toDays(task.recurringInterval());
                Calendar cal = CalendarUpdate.getCal();
                Calendar startTaskDate = task.startDate();
                Calendar nextTaskDate = task.nextDate();
                nextTaskDate.set(Calendar.HOUR, 0);
                nextTaskDate.set(Calendar.MINUTE, 0);
                nextTaskDate.set(Calendar.SECOND, 0);
                nextTaskDate.set(Calendar.MILLISECOND, 0);
                Calendar completedTaskDate = task.completedDate();

                Calendar todayDate = CalendarUpdate.getCalMidnight();


                System.out.println("task.id: " + task.id() + ". task.name: " + task.name());

                System.out.println("completedTaskDate:" + completedTaskDate);

                // set onDisplay to true if it should appear
                if (intervalDays > 0 && cal.getTimeInMillis() >= startTaskDate.getTimeInMillis()
                    && (completedTaskDate == null || completedTaskDate == todayDate)) {
                    task.setOnDisplay(true);
                    taskDao.setOnDisplay(task.id(), task.onDisplay());
                }

                // set onDisplay to false if its checked off
                if (intervalDays > 0 && (completedTaskDate != null && completedTaskDate.getTimeInMillis() < todayDate.getTimeInMillis())) {
                    task.setOnDisplay(false);
                    taskDao.setOnDisplay(task.id(), task.onDisplay());
                }

                // set onDisplay to false if recurring task expired
                if (intervalDays > 0 && nextTaskDate.getTimeInMillis() <= todayDate.getTimeInMillis()) {
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

                System.out.println("nexttaskdate" + nextTaskDate);
                System.out.println("cal" + cal);
                System.out.println("nexttaskdate in millis" + nextTaskDate.getTimeInMillis());
                System.out.println("cal in millis" + cal.getTimeInMillis());
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
                            null, // completedDate
                            task.isFifthWeekOfMonth(),
                            task.context() // completedDate
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
                            // if first recurring task was the fifth week of month
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            int nextTaskDateWeekInMonth = newNextTaskDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                            if (recurringTask.isFifthWeekOfMonth()) {
                                Calendar mockNextMonthTaskDate = (Calendar) nextTaskDate.clone();
                                for (int i=0; i<4; i++) {
                                    mockNextMonthTaskDate.add(Calendar.WEEK_OF_YEAR, 1);
                                }
                                System.out.println("mockNextMonthTaskDate: " + sdf.format(mockNextMonthTaskDate.getTime()));

                                int maxWeeksInNextMonth = mockNextMonthTaskDate.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH);
                                // there is no 5th week the next month
                                System.out.println("maxWeeksInNextMonth: " + maxWeeksInNextMonth);
                                if (maxWeeksInNextMonth < 5) {
                                    nextTaskDateWeekInMonth = maxWeeksInNextMonth;
                                } else {
                                    nextTaskDateWeekInMonth = 5;
                                }
                            }

                            newNextTaskDate.add(Calendar.MONTH, 1);
                            newNextTaskDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, nextTaskDateWeekInMonth);

                            System.out.println("nextTaskDateWeekInMonth: " + nextTaskDateWeekInMonth);
                            System.out.println("Next Task Date: " + sdf.format(nextTaskDate.getTime()));


//                            newNextTaskDate.add(Calendar.WEEK_OF_YEAR, 1);
//                            while (newNextTaskDate.get(Calendar.DAY_OF_WEEK_IN_MONTH) != nextTaskDateWeekInMonth) {
//                                newNextTaskDate.add(Calendar.WEEK_OF_YEAR, 1);
//                            }
                            break;
                        case 365:
                            newNextTaskDate.add(Calendar.YEAR, 1);
                            if (newNextTaskDate.get(Calendar.MONTH) == Calendar.FEBRUARY &&
                                    newNextTaskDate.get(Calendar.DAY_OF_MONTH) == 29 &&
                                    newNextTaskDate.getActualMaximum(Calendar.DAY_OF_MONTH) == 29) {
                                newNextTaskDate.add(Calendar.DATE, 1);
                            }
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

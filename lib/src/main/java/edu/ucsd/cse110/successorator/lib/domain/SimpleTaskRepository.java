package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleTaskRepository implements TaskRepo, TaskRepository {
    private final InMemoryDataSource dataSource;

    public SimpleTaskRepository(InMemoryDataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Task> find(int id)
    {
        return dataSource.getTaskSubject(id);
    }

    @Override
    public Subject<List<Task>> findAll() {
        return dataSource.getAllTasksSubjects();
    }

    @Override
    public void save(Task task) {
        dataSource.putTask(task);
    }

    @Override
    public void save(List<Task> tasks) {
        dataSource.putTasks(tasks);
    }

    @Override
    public void remove(int id) {
        dataSource.removeTask(id);
    }

    @Override
    public void append(Task task) {
        dataSource.putTask(
                task.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
//        dataSource.putTask(
//                task.withSortOrder(dataSource.getMaxCompleteSortOrder() + 1)
//        );
    }

    @Override
    public void prepend(Task task) {
        System.out.println("TaskRepository.java: prepend");
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putTask(
                task.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }

    @Override
    public void setComplete(int id, boolean status) {
        dataSource.setComplete(id, status);

    }

    @Override
    public void setType(int id, String type) {
        dataSource.setType(id, type);
    }

    public void removeCompleted(){
        dataSource.removeCompleted();
    }

    public Subject<List<Task>> filterTomorrowTasks(){
        return dataSource.filterTomorrowTasks();
    }

    public Subject<List<Task>> filterTodayTasks(){
        return dataSource.filterTodayTasks();
    }

    public Subject<List<Task>> filterRecurringTasks(){
        return dataSource.filterRecurringTasks();
    }

    public Subject<List<Task>> filterTasksByTypeAndContext(String type, String context){
        return dataSource.filterTasksByTypeAndContext(type, context);
    }

    @Override
    public void generateNextRecurringTasks() {

    }

    @Override
    public void setOnDisplays() {

    }

    @Override
    public void setTaskCompletedDate(Task task) {

    }


}

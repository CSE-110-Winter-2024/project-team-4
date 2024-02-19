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
        System.out.println("TaskRepository.java: append");
        dataSource.putTask(
                task.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
//        dataSource.putTask(
//                task.withSortOrder(dataSource.getCompleteSortOrder())
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
        System.out.println("STR IN SET COMPLETE METHOD");
        dataSource.setComplete(id, status);
        var task = dataSource.getTask(id);

        // if we mark task as complete
        if(status)
        {
            // check if uncompleted tasks are after this task
            if(task.sortOrder() != dataSource.getCompleteSortOrder())
            {
                System.out.println("getcomplete: " + dataSource.getCompleteSortOrder());
                // if there are uncompleted tasks after this task, decrease their sort orders by 1
                dataSource.shiftSortOrders(task.sortOrder()+1, dataSource.getCompleteSortOrder(), -1);
                dataSource.putTask(
                        task.withSortOrder(dataSource.getCompleteSortOrder() + 1)
                );
            }

            else
            {
                dataSource.putTask(
                        task.withSortOrder(dataSource.getCompleteSortOrder() + 1)
                );
            }
        }

        // if we mark as incomplete
        else
        {
            if(task.sortOrder() != dataSource.getMinSortOrder())
            {
                dataSource.shiftSortOrders(0, task.sortOrder(), 1);
                dataSource.putTask(
                        task.withSortOrder(1)
                );

            }

            else
            {
                dataSource.putTask(
                        task.withSortOrder(1)
                );
            }

        }
    }
}

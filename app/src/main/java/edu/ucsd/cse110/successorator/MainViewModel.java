package edu.ucsd.cse110.successorator;

import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepo;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

public class MainViewModel extends ViewModel {
    private final TaskRepository taskRepository;

    private final MutableSubject<List<Task>> orderedTasks;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    }
            );

    public MainViewModel(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
        this.orderedTasks = new SimpleSubject<>();

        taskRepository.findAll().observe(tasks -> {
            if (tasks == null) return;

//            var newOrderedTasks = tasks.stream()
//                    .sorted(Comparator.comparingInt(Task::sortOrder))
//                    .sorted(Comparator.comparing(Task::complete))
//                    .collect(Collectors.toList());
            var newOrderedTasks = new ArrayList<>(tasks);
            orderedTasks.setValue(newOrderedTasks);
        });

    }


    public Subject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }

    public void append(Task task) {
        System.out.println("MainViewModel append");
        System.out.println("task: " + task);
        System.out.println("task.id: " + task.id() + ". task.name: " + task.name());
        taskRepository.append(task);
    }

    public void prepend(Task task) {
        taskRepository.prepend(task);
    }

    public void remove(int id) {
        taskRepository.remove(id);
    }

    public void removeCompleted(){
        taskRepository.removeCompleted();
    }

    public int getCount() {
        return orderedTasks.getValue().size();
    }

    public TaskRepository taskRepository() { return taskRepository; }

    public void getTomorrowTasks() {
        taskRepository.filterTomorrowTasks().observe(tasks -> {
            if (tasks == null) return;

            var newOrderedTasks = new ArrayList<>(tasks);
            orderedTasks.setValue(newOrderedTasks);
        });
    }

    public void getTodayTasks() {
        taskRepository.filterTodayTasks().observe(tasks -> {
            if (tasks == null) return;

            var newOrderedTasks = new ArrayList<>(tasks);
//            System.out.println("TODAY TASKS: " + newOrderedTasks);
            orderedTasks.setValue(newOrderedTasks);
        });

    }

    public void getTasksByTypeAndContext(String type, String context) {
//        System.out.println("MainViewModel getTasksByTypeAndContext");
        taskRepository.filterTasksByTypeAndContext(type, context).observe(tasks -> {
            if (tasks == null) return;

            var newOrderedTasks = new ArrayList<>(tasks);
            orderedTasks.setValue(newOrderedTasks);

//            System.out.println("orderedTasks: " + newOrderedTasks);
        });
    }

    public void setOrderedTasks(){
        taskRepository.sortTasksByContext().observe(tasks -> {
            if (tasks == null) return;

            var newOrderedTasks = new ArrayList<>(tasks);
            orderedTasks.setValue(newOrderedTasks);
        });
    }

    public void getRecurringTasks() {
        taskRepository.filterRecurringTasks().observe(tasks -> {
            if (tasks == null) return;

            var newOrderedTasks = new ArrayList<>(tasks);
            orderedTasks.setValue(newOrderedTasks);
        });
    }

    public void getPendingTasks() {
        taskRepository.filterPendingTasks().observe(tasks -> {
            if (tasks == null) return;

            var newOrderedTasks = new ArrayList<>(tasks);
            orderedTasks.setValue(newOrderedTasks);
        });
    }



    public void setComplete(int id, boolean status) {taskRepository.setComplete(id, status);}

    public void setType(int id, String type) {taskRepository.setType(id, type);}


}

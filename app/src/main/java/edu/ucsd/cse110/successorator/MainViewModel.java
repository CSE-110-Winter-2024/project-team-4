package edu.ucsd.cse110.successorator;

import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
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
        taskRepository.append(task);
    }

    public void prepend(Task task) {
        taskRepository.prepend(task);
    }

    public void remove(int id) {
        taskRepository.remove(id);
    }


    public void setComplete(int id, boolean status) {
        System.out.println("MVM SETCOMPLETE");
        taskRepository.setComplete(id, status);

    }

//    public void moveTaskTop()
//    {
//        var tasks = this.getOrderedTasks().getValue();
//        var currSortOrder = 0;
//
//        for(Task t: tasks)
//        {
//            if(t.sortOrder() == -1)
//            {
//                currSortOrder++;
//                continue;
//            }
//
//            currSortOrder = t.sortOrder();
//
//            if(t.complete())
//            {
//                break;
//            }
//        }
//    }


}

package edu.ucsd.cse110.successorator;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModelTest {

    @Test
    public void append() {
        Task task = new Task(1, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task, task2);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.append(task3);
        assertEquals(3, model.getCount());

        List<Task> tas = List.of(task, task2, task3);
        assertArrayEquals(tas.toArray(), model.getOrderedTasks().getValue().toArray());
    }

    @Test
    public void prepend() {
        Task task = new Task(1, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        Task task4 = new Task(2, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task5 = new Task(3,"name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task4, task5);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.prepend(task);
        assertEquals(3, model.getCount());

        List<Task> tas = List.of(task, task2, task3);
        assertArrayEquals(tas.toArray(), model.getOrderedTasks().getValue().toArray());
    }

    @Test
    public void remove() {
        Task task = new Task(1, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        Task task4 = new Task(2, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task5 = new Task(3,"name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task, task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.remove(1);
        assertEquals(2, model.getCount());

        List<Task> tas = List.of(task4, task5);
        SimpleSubject<List<Task>> simp = new SimpleSubject<List<Task>>();
        simp.setValue(tas);


        Subject<List<Task>> simpo = model.getOrderedTasks();
        assertArrayEquals(simp.getValue().toArray(), simpo.getValue().toArray());

        model.remove(2);
        assertEquals(1, model.getCount());
    }

    @Test
    public void removeCompleted() {
        Task task = new Task(1, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",true, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        Task task4 = new Task(3,"name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");



        List<Task> tasks = List.of(task, task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.removeCompleted();
        assertEquals(2, model.getCount());

        List<Task> tas = List.of(task, task4);
        assertArrayEquals(tas.toArray(), model.getOrderedTasks().getValue().toArray());

    }

    @Test
    public void setComplete(){
        Task task2 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.setComplete(2, true);

        Task task5 = new Task(2, "name",true, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task6 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> taskss = List.of(task5, task6);

        assertArrayEquals(taskss.toArray(), model.getOrderedTasks().getValue().toArray());
    }

    @Test
    public void getTodayTasks(){
        Task task = new Task(1, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",false, 2, "date", "Tomorrow", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task, task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.getTodayTasks();
        assertEquals(2, model.getCount());

        List<Task> taskss = List.of(task, task3);
        assertArrayEquals(taskss.toArray(), model.getOrderedTasks().getValue().toArray());
    }

    @Test
    public void getTomorrowTasks(){
        Task task = new Task(1, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",false, 2, "date", "Tomorrow", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task, task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.getTomorrowTasks();
        assertEquals(1, model.getCount());

        List<Task> taskss = List.of(task2);
        assertArrayEquals(taskss.toArray(), model.getOrderedTasks().getValue().toArray());
    }

    //@Test
   /* public void getPendingTasks() {
        Task task = new Task(1, "name", false, 1, "date", "Pending", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name", false, 2, "date", "Tomorrow", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3, "name", false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task, task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.getPendingTasks();
        assertEquals(1, model.getCount());

        List<Task> taskss = List.of(task);
        assertArrayEquals(taskss.toArray(), model.getOrderedTasks().getValue().toArray());
    }
*/

    @Test
    public void moveOver() {
        Task task = new Task(1, "name",false, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",true, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Tomorrow", 0, null, true, null, true, null, "H");

        Task task4 = new Task(3,"name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task, task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.removeCompleted();
        List<Task> tas = List.of(task, task4);
        model.getTodayTasks();
        assertArrayEquals(tas.toArray(), model.getOrderedTasks().getValue().toArray());
    }

    @Test
    public void setOrderedTasks() {
        Task task = new Task(1, "name",true, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "W");
        Task task3 = new Task(3,"name",false, 3, "date", "Tomorrow", 0, null, true, null, true, null, "E");

        Task task5 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "W");
        Task task6 = new Task(3,"name",false, 3, "date", "Tomorrow", 0, null, true, null, true, null, "E");
        Task task4 = new Task(1, "name",true, 1, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task, task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.setOrderedTasks();
        List<Task> tas = List.of(task5, task6, task4);
        assertArrayEquals(tas.toArray(), model.getOrderedTasks().getValue().toArray());

    }

    @Test
    public void getTasksByTypeAndContext() {
        Task task = new Task(1, "name",true, 1, "date", "Today", 0, null, true, null, true, null, "H");
        Task task2 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "W");
        Task task3 = new Task(3,"name",false, 3, "date", "Tomorrow", 0, null, true, null, true, null, "E");

        Task task5 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "W");

        List<Task> tasks = List.of(task, task2, task3);

        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);


        model.getTasksByTypeAndContext("Today", "W");
        List<Task> tas = List.of(task5);
        assertArrayEquals(tas.toArray(), model.getOrderedTasks().getValue().toArray());
    }

    @Test
    public void setType() {
        Task task2 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task3 = new Task(3,"name",false, 3, "date", "Pending", 0, null, true, null, true, null, "H");

        List<Task> tasks = List.of(task2, task3);
        InMemoryDataSource inmd = new InMemoryDataSource();
        inmd.putTasks(tasks);

        var dataSource = new SimpleTaskRepository(inmd);
        var model = new MainViewModel(dataSource);

        model.setType(3, "Today");

        Task task5 = new Task(2, "name",false, 2, "date", "Today", 0, null, true, null, true, null, "H");
        Task task6 = new Task(3,"name",false, 3, "date", "Today", 0, null, true, null, true, null, "H");

        List<Task> taskss = List.of(task5, task6);

        assertArrayEquals(taskss.toArray(), model.getOrderedTasks().getValue().toArray());
    }

}
package edu.ucsd.cse110.successorator.lib.data;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

/**
 * Class used as a sort of "database" of decks and tasks that exist. This
 * will be replaced with a real database in the future, but can also be used
 * for testing.
 */
public class InMemoryDataSource {
    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;


    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, MutableSubject<Task>> taskSubjects
            = new HashMap<>();
    private final MutableSubject<List<Task>> allTasksSubject
            = new SimpleSubject<>();

    public InMemoryDataSource() {
    }

    public final static List<Task> DEFAULT_TASKS = List.of(
//            new Task(0, "SRP", false, 0),
//            new Task(1, "OCP", false, 1),
//            new Task(2, "LSP", false, 2),
//            new Task(3, "ISP", false,3),
//            new Task(4, "DIP", false, 4),
//            new Task(5, "LKP", false, 5)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.putTasks(DEFAULT_TASKS);
        return data;
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Subject<Task> getTaskSubject(int id) {
        if (!taskSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Task>();
            subject.setValue(getTask(id));
            taskSubjects.put(id, subject);
        }
        return taskSubjects.get(id);
    }

    public Subject<List<Task>> getAllTasksSubjects() {
        return allTasksSubject;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    public void putTask(Task task) {
        System.out.println("InMemoryDataSource: begin putTask");
        var fixedTask = preInsert(task);
        System.out.println("tasks.size(): " + tasks.size());
        if (tasks.size() == 0) {
            fixedTask = fixedTask.withSortOrder(0);
        }
        System.out.println("fixedTask sortOrder: " + fixedTask.sortOrder());


        tasks.put(fixedTask.id(), fixedTask);
        postInsert();
        assertSortOrderConstraints();
        System.out.println("InMemoryDataSource: after assertSortOrderConstraints();");

        if (taskSubjects.containsKey(fixedTask.id())) {
            taskSubjects.get(fixedTask.id()).setValue(fixedTask);
        }
        allTasksSubject.setValue(getTasks());
        System.out.println("InMemoryDataSource: end putTask");
    }


    public void putTasks(List<Task> tasksList) {
        var fixedTasks = tasksList.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedTasks.forEach(task -> tasks.put(task.id(), task));
        postInsert();
        assertSortOrderConstraints();

        fixedTasks.forEach(task -> {
            if (taskSubjects.containsKey(task.id())) {
                taskSubjects.get(task.id()).setValue(task);
            }
        });
        allTasksSubject.setValue(getTasks());
    }

    public void removeTask(int id) {
        var task = tasks.get(id);
        var sortOrder = task.sortOrder();

        tasks.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(null);
        }
        allTasksSubject.setValue(getTasks());
    }

    public void shiftSortOrders(int from, int to, int by) {
        var taskList = tasks.values().stream()
                .filter(task -> task.sortOrder() >= from && task.sortOrder() <= to)
                .map(task -> task.withSortOrder(task.sortOrder() + by))
                .collect(Collectors.toList());
        System.out.println("InMemoryDataSource: shiftSortOrders taskList");
        System.out.println(taskList);

        putTasks(taskList);

        var sortOrders = tasks.values().stream()
                .map(Task::sortOrder)
                .collect(Collectors.toList());

        System.out.println("InMemoryDataSource: shiftSortOrders sortOrders");
        System.out.println(sortOrders);
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * tasks inserted have an id, and updates the nextId if necessary.
     */
    private Task preInsert(Task task) {
        var id = task.id();
        if (id == null) {
            // If the task has no id, give it one.
            task = task.withId(nextId++);
        }
        else if (id > nextId) {
            // If the task has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load task like in fromDefault().
            nextId = id + 1;
        }

        return task;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * <p></p>
     * Will throw an AssertionError if any of the constraints are violated,
     * which should never happen. Mostly here to make sure I (Dylan) don't
     * write incorrect code by accident!
     */
    private void assertSortOrderConstraints() {
        System.out.println("InMemoryDataSource: assertSortOrderConstraints");

        var sortOrders = tasks.values().stream()
                .map(Task::sortOrder)
                .collect(Collectors.toList());
        System.out.println("InMemoryDataSource: after var sortOrders");

        assert sortOrders.stream().allMatch(i -> i >= 0);
        System.out.println(sortOrders);
        System.out.println("InMemoryDataSource: after sortOrders.stream()");

        assert sortOrders.size() == sortOrders.stream().distinct().count();

        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);

    }
}

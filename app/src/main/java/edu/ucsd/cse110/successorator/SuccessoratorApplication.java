package edu.ucsd.cse110.successorator;

import static android.app.PendingIntent.getActivity;

import android.app.Application;

import androidx.room.Room;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.TaskDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.MainActivity;
import edu.ucsd.cse110.successorator.MainViewModel;



public class SuccessoratorApplication extends Application {

    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                TaskDatabase.class,
                "task-database"
        ).allowMainThreadQueries().build();

        this.taskRepository = new RoomTaskRepository(database.taskDao());

    /*
        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        //var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        //isFirstRun &&
        if(database.taskDao().count() != 0) {
            ((MainActivity)getActivity()).swapFragments();  //HOW TO CALL GETACTIVITY() FROM APPLICATION? HERE

            //sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
        }
        
     */


//        this.dataSource = InMemoryDataSource.fromDefault();
//        this.taskRepository = new SimpleTaskRepository(dataSource);
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}

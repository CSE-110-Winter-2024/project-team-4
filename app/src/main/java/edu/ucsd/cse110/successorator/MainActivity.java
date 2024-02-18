package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.successorator.data.db.TaskDatabase;
import edu.ucsd.cse110.successorator.databinding.FragmentDialogCreateCardBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentNoTasksBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.ui.taskList.TaskListFragment;
import edu.ucsd.cse110.successorator.ui.taskList.dialog.AddTaskDialogFragment;

import edu.ucsd.cse110.successorator.lib.domain.CalendarUpdate;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private Date date;
    //private Calendar cal;

//    private FragmentDialogCreateCardBinding view;
private FragmentNoTasksBinding view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //var view = ActivityMainBinding.inflate(getLayoutInflater(), null, false);
        //view.placeholderText.setText(R.string.hello_world);
        //setContentView(view.getRoot());

        setContentView(R.layout.activity_main);

        Calendar cal = CalendarUpdate.getCal();
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());

        TextView dateTextView = findViewById(R.id.date_box);
        dateTextView.setText(dateFormat.toString());

        var database = Room.databaseBuilder(
                getApplicationContext(),
                TaskDatabase.class,
                "task-database"
        ).allowMainThreadQueries().build();

        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        //var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        //isFirstRun &&
        if(database.taskDao().count() != 0) {
            swapFragments();  //HOW TO CALL GETACTIVITY() FROM APPLICATION? HERE
            //sharedPreferences.edit().putBoolean("isFirstRun", false).apply();
        }

    }



//    @Nullable
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        this.view = .inflate(inflater, container, false);
//
//        view.addTaskButton.setOnClickListener(v -> {
//            var dialogFragment = AddTaskDialogFragment.newInstance();
//            dialogFragment.show(getParentFragmentManager(), "CreateCardDialogFragment");
//        });
//
//        return view.getRoot();
//    }

    public void incrementCurrentDate(View view){
        CalendarUpdate.incrementDateBy1();
        Calendar cal = CalendarUpdate.getCal();
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        TextView dateTextView = findViewById(R.id.date_box);
        dateTextView.setText(dateFormat.toString());
    }

    public void swapFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, TaskListFragment.newInstance())
                .commit();

    }



}

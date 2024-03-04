package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import edu.ucsd.cse110.successorator.data.db.TaskDatabase;
import edu.ucsd.cse110.successorator.databinding.FragmentNoTasksBinding;
import edu.ucsd.cse110.successorator.ui.noTasksList.NoTasksFragment;
import edu.ucsd.cse110.successorator.ui.taskList.TaskListFragment;

import edu.ucsd.cse110.successorator.lib.domain.CalendarUpdate;
import edu.ucsd.cse110.successorator.ui.taskList.dialog.ModelFetch;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Date date;
    //private Calendar cal;

    //    private FragmentDialogCreateCardBinding view;
    private FragmentNoTasksBinding view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        var database = Room.databaseBuilder(
                getApplicationContext(),
                TaskDatabase.class,
                "task-database"
        ).allowMainThreadQueries().build();

        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);

        //isFirstRun &&
        if (database.taskDao().count() != 0) {
            swapFragments();  //HOW TO CALL GETACTIVITY() FROM APPLICATION? HERE
        }

//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(1000);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                TextView tdate = (TextView) findViewById(R.id.date_box);
//                                long date = System.currentTimeMillis();
//                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, M/d");
//                                String dateString = sdf.format(date);
//                                tdate.setText(dateString);
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                }
//            }
//        };
//        t.start();

        CalendarUpdate.initializeCal();
        Calendar cal = CalendarUpdate.getCal();
        SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
        String formattedDate = customFormat.format(cal.getTime());
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());

        TextView dateTextView = findViewById(R.id.date_box);
        dateTextView.setText(formattedDate);

    }

    @Override
    public void onResume() {
        super.onResume();
        CalendarUpdate.initializeCal();
        //Calendar cal = CalendarUpdate.getCal();

        TextView tdate = (TextView) findViewById(R.id.date_box);
        String currentDate = (String)tdate.getText();
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, M/d");
        String dateString = sdf.format(date);
        tdate.setText(dateString);


        if(!currentDate.equals(dateString)){
            MainViewModel activityModel = ModelFetch.getModel();
            activityModel.removeCompleted();

            var database = Room.databaseBuilder(
                    getApplicationContext(),
                    TaskDatabase.class,
                    "task-database"
            ).allowMainThreadQueries().build();

            var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
            //var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

            //isFirstRun &&
            if(database.taskDao().count() == 0) {
                swapFragmentstoNoTasks();
            }
        }
        Spinner spinner = findViewById(R.id.fromspin);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Options,
                android.R.layout.simple_spinner_item
        );

//        ArrayAdapter ad
//                = new ArrayAdapter(
//                this,
//                android.R.layout.simple_spinner_item,
//                courses);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }


    public void incrementCurrentDate(View view) {
        CalendarUpdate.incrementDateBy1();
        Calendar cal = CalendarUpdate.getCal();
        SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");

        String formattedDate = customFormat.format(cal.getTime());
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
        TextView dateTextView = findViewById(R.id.date_box);
        dateTextView.setText(formattedDate);

        MainViewModel activityModel = ModelFetch.getModel();
        activityModel.removeCompleted();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                TaskDatabase.class,
                "task-database"
        ).allowMainThreadQueries().build();

        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);

        if (database.taskDao().count() == 0) {
            swapFragmentstoNoTasks();
        }
    }

    public void swapFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, TaskListFragment.newInstance())
                .commit();

    }

    public void swapFragmentstoNoTasks() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, NoTasksFragment.newInstance())
                .commit();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        System.out.println("MainActivity onItemSelected");

        switch (selectedItem) {
            case "Today":
                // Do something for Today
                System.out.println("Today");
                break;
            case "Tomorrow":
                // Do something for Tomorrow
                System.out.println("Tomorrow");
                break;
            case "Recurring":
                // Do something for Recurring
                System.out.println("Recurring");
                break;
            case "Pending":
                // Do something for Pending
                System.out.println("Pending");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("MainActivity onNothingSelected");

    }
}

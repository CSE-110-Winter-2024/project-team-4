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
import androidx.lifecycle.ViewModelProvider;
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
    private MainViewModel activityModel;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);


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

        CalendarUpdate.initializeCal();
        Calendar cal = CalendarUpdate.getCal();
        // loop through all tasks and call function to create recurring tasks
        System.out.println("MainActivity onCreate");

        activityModel.taskRepository().generateNextRecurringTasks();
        activityModel.taskRepository().setOnDisplays();

        SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
        String formattedDate = customFormat.format(cal.getTime());
        var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());

        TextView dateTextView = findViewById(R.id.date_box);
        dateTextView.setText(formattedDate);

        MainViewModel model = ModelFetch.getModel();
        model.getTodayTasks();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Spinner spin = findViewById(R.id.fromspin);
                                String status = spin.getSelectedItem().toString();

                                if(status.equals("Today")){
                                    model.getTodayTasks();
                                }
                                else if(status.equals("Tomorrow")){
                                    model.getTomorrowTasks();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        CalendarUpdate.initializeCal();
        //Calendar cal = CalendarUpdate.getCal();

        // loop through all tasks and call function to create recurring tasks
        System.out.println("MainActivity onResume");
        activityModel.taskRepository().generateNextRecurringTasks();
//        activityModel.taskRepository().setOnDisplays();

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
        spinner.setSelection(1);
        MainViewModel model = ModelFetch.getModel();
        model.getTodayTasks();
    }


    public void incrementCurrentDate(View view) {
        CalendarUpdate.incrementDateBy1();
        Calendar cal = CalendarUpdate.getCal();
        // loop through all tasks and call function to create recurring tasks
        System.out.println("MainActivity incrementCurrentDate");
        activityModel.taskRepository().generateNextRecurringTasks();
        activityModel.taskRepository().setOnDisplays();

        SimpleDateFormat customFormat = new SimpleDateFormat("EEE, M/d");

        Spinner spin = findViewById(R.id.fromspin);
        TextView dateTextView = findViewById(R.id.date_box);
        if (spin.getSelectedItem().equals("Today")){
            String formattedDate = customFormat.format(cal.getTime());
            var dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
            dateTextView.setText("Today, " + formattedDate);
        } else if(spin.getSelectedItem().equals("Tomorrow")){
            Calendar cala = (Calendar) cal.clone();
            cala.add(Calendar.DATE, 1);
            String dateString = customFormat.format(cala.getTime());
            dateTextView.setText("Tomorrow, " + dateString);
        } else if (spin.getSelectedItem().equals("Pending") || spin.getSelectedItem().equals("Recurring")) {
            dateTextView.setText("");
        }

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

    public String getSpinnerStatus(){
        Spinner spinner = findViewById(R.id.fromspin);
        String selectedItem = spinner.getSelectedItem().toString();
        return selectedItem;
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
        int previous = 1;

        switch (selectedItem) {
            case "Today":
                // Do something for Today
                System.out.println("Today");

                Spinner spinner = findViewById(R.id.fromspin);
                Calendar cal = (Calendar) CalendarUpdate.getCal().clone();
                TextView dateTextView = findViewById(R.id.date_box);
                SimpleDateFormat customFormat = new SimpleDateFormat("EEE M/d");
                String dateString = customFormat.format(cal.getTime());
                dateTextView.setText("Today, " + dateString);

                MainViewModel model = ModelFetch.getModel();
                model.getTodayTasks();
                break;
            case "Tomorrow":
                // Do something for Tomorrow
                System.out.println("Tomorrow");
                Spinner spin = findViewById(R.id.fromspin);
                Calendar cala = (Calendar) CalendarUpdate.getCal().clone();
                cala.add(Calendar.DATE, 1);
                TextView dateTextViewa = findViewById(R.id.date_box);
                SimpleDateFormat customFormats = new SimpleDateFormat("EEE M/d");
                String dateStringa = customFormats.format(cala.getTime());
                dateTextViewa.setText("Tomorrow, " + dateStringa);

                MainViewModel modela = ModelFetch.getModel();
                modela.getTomorrowTasks();
                break;
            case "Recurring":
                // Do something for Recurring
                ((TextView)findViewById(R.id.date_box)).setText("");
                System.out.println("Recurring");
                break;
            case "Pending":
                // Do something for Pending
                ((TextView)findViewById(R.id.date_box)).setText("");
                System.out.println("Pending");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        System.out.println("MainActivity onNothingSelected");

    }
}

package edu.ucsd.cse110.successorator.ui.taskList.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.ucsd.cse110.successorator.MainActivity;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.lib.domain.CalendarUpdate;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class AddRecurringTaskDialogFragment extends DialogFragment {
    private edu.ucsd.cse110.successorator.databinding.FragmentDialogRecurringMenuBinding view;
    private MainViewModel activityModel;

    private String context = "";

    public AddRecurringTaskDialogFragment()
    {

    }


    public static AddRecurringTaskDialogFragment newInstance() {
        var fragment = new AddRecurringTaskDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = edu.ucsd.cse110.successorator.databinding.FragmentDialogRecurringMenuBinding.inflate(getLayoutInflater());
        String[] choices = {"one time", "daily", "weekly", "monthly", "yearly"};

        view.recurrenceOptions.setVisibility(View.INVISIBLE);
        view.recurrenceField.setVisibility(View.GONE);
        MainActivity mainActivity = (MainActivity) getActivity();
        System.out.println("SPINNER STATUS: " + mainActivity.getSpinnerStatus());

//        CalendarUpdate.initializeCal();
        Calendar cal = CalendarUpdate.getCal();
        SimpleDateFormat weeklyFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat monthlyFormat = new SimpleDateFormat("F");
        SimpleDateFormat yearlyFormat = new SimpleDateFormat("M/d");
        SimpleDateFormat recurringOnFormat = new SimpleDateFormat("MM/dd/Y");
        String numberSuffix = "";
        switch(mainActivity.getSpinnerStatus()) {
            case "Tomorrow":
//                cal.add(Calendar.DATE,1);
                Calendar cala = (Calendar) cal.clone();
                cala.add(Calendar.DATE, 1);
                view.recurrenceOptions.setVisibility(View.VISIBLE);
                view.recurrenceField.setVisibility(View.INVISIBLE);
                view.starting.setVisibility(View.GONE);
                view.oneTime.setVisibility(View.VISIBLE);
                view.weekly.setText("weekly on " + weeklyFormat.format(cala.getTime()).substring(0,2));

                switch(monthlyFormat.format(cala.getTime())){
                    case "1":
                        numberSuffix = "st";
                        break;
                    case "2":
                        numberSuffix = "nd";
                        break;
                    case "3":
                        numberSuffix = "rd";
                        break;
                    default :
                        numberSuffix = "th";
                }
                view.monthly.setText("monthly on " + monthlyFormat.format(cala.getTime()) + numberSuffix + " " + weeklyFormat.format(cala.getTime()).substring(0,2));
                view.yearly.setText("yearly on " + yearlyFormat.format(cala.getTime()));
                break;
            case "Today":
                view.recurrenceOptions.setVisibility(View.VISIBLE);
                view.recurrenceField.setVisibility(View.INVISIBLE);
                view.starting.setVisibility(View.GONE);
                view.oneTime.setVisibility(View.VISIBLE);
                view.weekly.setText("weekly on " + weeklyFormat.format(cal.getTime()).substring(0,2));
//                String numberSuffix = "";
                switch(monthlyFormat.format(cal.getTime())){
                    case "1":
                        numberSuffix = "st";
                        break;
                    case "2":
                        numberSuffix = "nd";
                        break;
                    case "3":
                        numberSuffix = "rd";
                        break;
                    default :
                        numberSuffix = "th";
                }
                view.monthly.setText("monthly on " + monthlyFormat.format(cal.getTime()) + numberSuffix + " " + weeklyFormat.format(cal.getTime()).substring(0,2));
                view.yearly.setText("yearly on " + yearlyFormat.format(cal.getTime()));
                break;
            case "Pending":
                view.recurrenceOptions.setVisibility(View.INVISIBLE);
                view.starting.setVisibility(View.GONE);
                break;
            case "Recurring":
                view.recurrenceOptions.setVisibility(View.VISIBLE);
                view.recurrenceField.setText(recurringOnFormat.format(cal.getTime()));
                view.recurrenceField.setVisibility(View.VISIBLE);
                view.oneTime.setVisibility(View.GONE);
                view.starting.setVisibility(View.VISIBLE);
                view.daily.setText("daily...");
                view.weekly.setText("weekly...");
                view.monthly.setText("monthly...");
                view.yearly.setText("yearly...");
                break;
            default:
                view.recurrenceOptions.setVisibility(View.VISIBLE);
                view.recurrenceField.setVisibility(View.INVISIBLE);
                view.starting.setVisibility(View.GONE);
                view.oneTime.setVisibility(View.VISIBLE);
        }



        Button dialogSaveButton = (Button) view.saveButton;
        Dialog recurDialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();

        TextView home = (TextView) view.homeButton;
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = "H";
            }
        });

        TextView work = (TextView) view.workButton;
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = "W";
            }
        });

        TextView school = (TextView) view.schoolButton;
        school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = "S";
            }
        });

        TextView errand = (TextView) view.errandButton;
        errand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = "E";
            }
        });
        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddRecurringTaskDialogFragment", "This is a debug message");
                var name = view.recurringInput.getText().toString();
                var recurringDate = view.recurrenceField.getText().toString();
                String recurrenceText = "";
                if (!mainActivity.getSpinnerStatus().equals("Pending") && !context.equals("")){
                    // Retrieving dialog's selection for recurrence
                    int checkedRadio = view.recurrenceOptions.getCheckedRadioButtonId();

                    if (checkedRadio != -1) {
                        View selectedRecurrence = view.recurrenceOptions.findViewById(checkedRadio);

                        int radioIndex = view.recurrenceOptions.indexOfChild(selectedRecurrence);
                        RadioButton recurrence = (RadioButton) view.recurrenceOptions.getChildAt(radioIndex);
                        recurrenceText = recurrence.getText().toString();
                        System.out.println("recurrenceText: " + recurrenceText);
                    }
                }


                // Check that the required input string and radio selection are valid
                if(!name.equals("") && (!recurrenceText.equals("") || (mainActivity.getSpinnerStatus().equals("Pending") && !context.equals(""))))
                {
                    Calendar cal = CalendarUpdate.getCal();
                    SimpleDateFormat customFormat = new SimpleDateFormat("EEEE, M/d");
                    String formattedDate = customFormat.format(cal.getTime());

                    if(mainActivity.getSpinnerStatus().equals("Recurring")) {
                        formattedDate = recurringDate;
                    }

                    System.out.println("RECURRING DATE: " + formattedDate);


                    Task task;
                    Calendar taskStartDate = (Calendar) cal.clone();
                    System.out.println("cloned cal " + cal.getTimeInMillis());
                    if (mainActivity.getSpinnerStatus().equals("Tomorrow")) {
                        taskStartDate.add(Calendar.DATE, 1);
                    }
                    System.out.println("taskstartdate today/tmrw" + taskStartDate.getTimeInMillis());
                    if (!recurrenceText.equals("daily") && !recurrenceText.equals("daily...")) {
                         task = new Task(null, name, false, -1, formattedDate,
                                mainActivity.getSpinnerStatus(), 0, taskStartDate,
                                true, null, false, null, false, context);
                    }
                    else {
                        task = new Task(null, name, false, -1, formattedDate,
                                mainActivity.getSpinnerStatus(), 0, taskStartDate,
                                true, null, true, null, false, context);
                    }



                    if (!mainActivity.getSpinnerStatus().equals("Pending")){
                        System.out.println("ENTERED nexttask switch statement");
                        Calendar nextTaskDate = (Calendar) task.startDate().clone();
                        System.out.println("recurrence");
                        switch (recurrenceText.substring(0,5)) {
                            case "one-t":
                                task.setRecurringInterval(-1);
                                task.setNextDate(nextTaskDate);
                                break;
                            case "daily":
                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(1));
                                nextTaskDate.add(Calendar.DATE, 1);
                                task.setNextDate(nextTaskDate);
                                task.setName(task.name() + ", daily");
                                break;
                            case "weekl":
                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(7));
                                nextTaskDate.add(Calendar.WEEK_OF_YEAR, 1);
                                task.setNextDate(nextTaskDate);
                                task.setName(task.name() + ", weekly on " + weeklyFormat.format(cal.getTime()));
                                break;
                            case "month":
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(30));

                                int nextTaskDateWeekInMonth = nextTaskDate.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                                // if the recurrence starts on the 5th [SMTWTFS] of the month
                                if (nextTaskDateWeekInMonth == 5) {
                                    task.setIsFifthWeekOfMonth(true);
                                    Calendar mockNextMonthTaskDate = (Calendar) nextTaskDate.clone();
                                    for (int i=0; i<4; i++) {
                                        mockNextMonthTaskDate.add(Calendar.WEEK_OF_YEAR, 1);
                                    }
                                    System.out.println("mockNextMonthTaskDate: " + sdf.format(mockNextMonthTaskDate.getTime()));
                                    int maxWeeksInNextMonth = mockNextMonthTaskDate.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH);
                                    System.out.println("maxWeeksInNextMonth: " + maxWeeksInNextMonth);
                                    // there is no 5th week the next month
                                    if (maxWeeksInNextMonth < 5) {
                                        nextTaskDateWeekInMonth = maxWeeksInNextMonth;
                                    }
                                }
                                nextTaskDate.add(Calendar.MONTH, 1);
                                nextTaskDate.set(Calendar.DAY_OF_WEEK_IN_MONTH, nextTaskDateWeekInMonth);

                                System.out.println("nextTaskDateWeekInMonth: " + nextTaskDateWeekInMonth);
                                System.out.println("Next Task Date: " + sdf.format(nextTaskDate.getTime()));

                                task.setNextDate(nextTaskDate);
                                String numberSuffix = "";
                                switch(monthlyFormat.format(cal.getTime())){
                                    case "1":
                                        numberSuffix = "st";
                                        break;
                                    case "2":
                                        numberSuffix = "nd";
                                        break;
                                    case "3":
                                        numberSuffix = "rd";
                                        break;
                                    default :
                                        numberSuffix = "th";
                                }
                                task.setName(task.name() + ", monthly on " + monthlyFormat.format(cal.getTime()) + numberSuffix + " " + weeklyFormat.format(cal.getTime()));
                                break;
                            case "yearl":
                                task.setRecurringInterval(TimeUnit.DAYS.toMillis(365));
                                nextTaskDate.add(Calendar.YEAR, 1);
                                if (nextTaskDate.get(Calendar.MONTH) == Calendar.FEBRUARY &&
                                        nextTaskDate.get(Calendar.DAY_OF_MONTH) == 29 &&
                                        nextTaskDate.getActualMaximum(Calendar.DAY_OF_MONTH) == 29) {
                                    nextTaskDate.add(Calendar.DATE, 1);
                                }
                                task.setNextDate(nextTaskDate);
                                task.setName(task.name() + ", yearly on " + yearlyFormat.format(cal.getTime()));
                                break;
                            default:
                                System.out.println("Unknown recurrence.");
                        }

                    }

                    System.out.println("AddRecTaskDialogFrag onClick task = " + task);
                    System.out.println("task.id: " + task.id() + ". task.name: " + task.name());

                    activityModel.append(task);
                    
                    recurDialog.dismiss();


                    Calendar todayMidnight = CalendarUpdate.getCalMidnight();
                    Calendar tmrwMidnight = (Calendar) todayMidnight.clone();
                    tmrwMidnight.add(Calendar.DATE,1);
                    if ((recurrenceText.equals("daily") || recurrenceText.equals("daily...")) && task.startDate().equals(todayMidnight)) {
                        Calendar tomorrowDate = (Calendar) taskStartDate.clone();
                        tomorrowDate.add(Calendar.DATE, 1);
                        String formattedDate2 = customFormat.format(tomorrowDate.getTime());

                        var task2 = new Task(null, name + ", daily", false, -1, formattedDate2,
                                mainActivity.getSpinnerStatus(), TimeUnit.DAYS.toMillis(1), tomorrowDate,
                                false, null, false, null, false);

                        Calendar nextTaskDate2 = (Calendar) task2.startDate().clone();
                        nextTaskDate2.add(Calendar.DATE, 1);
                        task2.setNextDate(nextTaskDate2);
                        activityModel.append(task2);
                    } else if ((recurrenceText.equals("daily") || recurrenceText.equals("daily...")) && task.startDate().equals(tmrwMidnight)) {
                        Calendar nextDate = (Calendar) taskStartDate.clone();
                        nextDate.add(Calendar.DATE, 1);
                        String formattedDate2 = customFormat.format(nextDate.getTime());

                        var task2 = new Task(null, name + ", daily", false, -1, formattedDate2,
                                mainActivity.getSpinnerStatus(), TimeUnit.DAYS.toMillis(1), nextDate,
                                false, null, false, null, false);

                        Calendar nextTaskDate2 = (Calendar) task2.startDate().clone();
                        nextTaskDate2.add(Calendar.DATE, 1);
                        task2.setNextDate(nextTaskDate2);
                        activityModel.append(task2);
                    }



                    ((MainActivity)getActivity()).swapFragments();

                }

            }
        });

        return recurDialog;

    }



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        ModelFetch.setModel(activityModel);

    }

}
